package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.domain.Show;
import study.ticket.infrastructure.BookingRepository;
import study.ticket.infrastructure.redis.seat.RedisKeys;

import java.util.*;
import java.util.stream.Stream;

//@Service
@RequiredArgsConstructor
@Slf4j
// version6: Redis (Lua Script)
public class BookingServiceImplV6 implements BookingService {

    private final MemberService memberService;
    private final SeatService seatService;
    private final ShowService showService;

    private final BookingRepository bookingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
//    private final StringRedisTemplate redisTemplate;

    private static final int MAX_SEAT_COUNT = 2;
    private static final int SEAT_TTL = 60; // seconds
    private static final int BOOKED_TTL = 86400; // seconds

    @Override
    public Optional<Booking> findById(long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Optional<Booking> findByMemberId(String loginId) {
        return bookingRepository.findByMemberId(loginId);
    }

    @Override
    public List<Booking> findByIds(List<Long> ids) {
        return bookingRepository.findByIds(ids);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    @Transactional
    public void book(String loginId, long showId, List<Long> seatIds) {
        // 좌석 선점 확인
        assertValidateSeat(loginId, showId, seatIds);

        // 사용자 별 예매 개수 제한 (1인당 최대 2매)
        memberService.increaseBookingCount(loginId, showId, seatIds.size(), MAX_SEAT_COUNT);

        // 좌석 선점 (좌석 상태 변경)
        seatService.updateToBooked(showId, seatIds);

        Member member = memberService.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("아이디를 찾을 수 없습니다"));
        List<Seat> seats = seatService.findByIds(seatIds);
        Show show = showService.findById(showId).orElseThrow(() -> new IllegalStateException("공연을 찾을 수 없습니다"));

        List<Booking> bookings = Booking.of(member, show, seats);

        // 결제
        pay();

        // 예매 정보 등록
        save(bookings);

        // 좌석을 redis에 캐싱
        cacheBookedSeat(loginId, showId, seatIds);
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void save(List<Booking> bookings) {
        bookingRepository.save(bookings);
    }

    private void assertValidateSeat(String loginId, long showId, List<Long> seatIds) {
        // Lua Script
        RedisScript<Long> script = getLuaScript("script/redis/validateSeat.lua", Long.class);

        List<String> keys = getKeys(showId, seatIds);

        // ARGV: userId, seatCount, ttl
        Long result = redisTemplate.execute(
                script,
                keys,
                loginId,                         // ARGV[1]: 사용자 아이디
                String.valueOf(seatIds.size()),  // ARGV[2]: 예매할 좌석 개수
                String.valueOf(SEAT_TTL)         // ARGV[3]: TTL (초)
        );

        // 0: 좌석이 이미 선점됨, 1: 성공
        if (result == null) {
            throw new IllegalStateException("좌석 선점 중 오류가 발생했습니다.");
        }
        else if (result == -1) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        else if (result == 0) {
            throw new IllegalStateException("이미 선점된 좌석입니다.");
        }

        // result == 1이면 성공
    }

    private <T> RedisScript<T> getLuaScript(String resourcePath, Class<T> resultType) {
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource(resourcePath));
        script.setResultType(resultType);
        return script;
    }

    private List<String> getKeys(long showId, List<Long> seatIds) {
        return Stream.concat(
                RedisKeys.BOOKED.generateKeys(showId, seatIds).stream(),
                RedisKeys.PREEMPTED.generateKeys(showId, seatIds).stream()
        ).toList();
    }

    private void cacheBookedSeat(String loginId, long showId, List<Long> seatIds) {
        // Lua Script
        RedisScript<Long> script = getLuaScript("script/redis/cacheOfBookedSeat.lua", Long.class);

        List<String> keys = getKeys(showId, seatIds);

        // ARGV: userId, seatCount, ttl
        Long result = redisTemplate.execute(
                script,
                keys,
                loginId,                         // ARGV[1]: 사용자 아이디
                String.valueOf(seatIds.size()),  // ARGV[2]: 예매할 좌석 개수
                String.valueOf(BOOKED_TTL)       // ARGV[3]: TTL (초)
        );

        if (result == null) {
            throw new IllegalStateException("예매 중 오류가 발생했습니다.");
        }

        // result == 1이면 성공
    }
}
