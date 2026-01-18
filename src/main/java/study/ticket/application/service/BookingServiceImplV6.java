package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.BookingRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
// version6: Redis
public class BookingServiceImplV6 implements BookingService {

    private final MemberService memberService;
    private final SeatService seatService;
    private final BookingRepository bookingRepository;
    private final RedisTemplate<String, Object> redisTemplate;
//    private final StringRedisTemplate redisTemplate;

    private static final int MAX_SEAT_COUNT = 2;
    private static final int SEAT_TTL = 60; // seconds

    @Override
    public Optional<Booking> findById(String id) {
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
    public void book(String loginId, List<Long> seatIds) {
        Member member = memberService.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("아이디를 찾을 수 없습니다"));
        List<Seat> seats = seatService.findByIds(seatIds);

        // 좌석 선점 확인
        assertValidateSeat(seatIds, loginId);

        // 사용자 별 예매 개수 제한
        int updatedResult = memberService.increaseBookingCount(loginId, 1, seatIds.size(), MAX_SEAT_COUNT);
        if (updatedResult == 0) {
            throw new IllegalStateException("1인 최대 " + MAX_SEAT_COUNT + "매까지 예매 가능합니다.");
        }


        // 좌석 선점 (좌석 상태 변경) - 동시성 문제 발생
        seatService.updateToBooked(seatIds);

        // 한 명의 회원은 최대 2매까지 예매가능
        List<Booking> bookings = Booking.of(member, seats);

        // 결제
        pay();

        // 예매 정보 등록
        save(bookings);
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public void save(List<Booking> bookings) {
        bookingRepository.save(bookings);
    }

    private void assertValidateSeat(List<Long> seatIds, String loginId) {
        // Lua Script
        RedisScript<Long> script = getSeatPreemptScript();

        // KEYS: 좌석 키들 (seat:{seatId}) + 마지막에 사용자 예매 수 키 (user:booked:{userId})
        List<String> keys = createKeys(seatIds, loginId);

        // ARGV: userId, seatCount, maxSeatCount, ttl
        Long result = redisTemplate.execute(
                script,
                keys,
                loginId,                         // ARGV[1]: 사용자 아이디
                String.valueOf(seatIds.size()),  // ARGV[2]: 예매할 좌석 개수
                String.valueOf(MAX_SEAT_COUNT),  // ARGV[3]: 최대 예매 개수
                String.valueOf(SEAT_TTL)         // ARGV[4]: TTL (초)
        );

        // 결과 처리
        // -1: 사용자 제한 초과, 0: 좌석이 이미 선점됨, 1: 성공
        if (result == null) {
            throw new IllegalStateException("좌석 선점 중 오류가 발생했습니다.");
        }
//        else if (result == -1) {
//            throw new IllegalStateException("1인 최대 " + MAX_SEAT_COUNT + "매까지 예매 가능합니다.");
//        }
        else if (result == 0) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        // result == 1이면 성공
    }

    private RedisScript<Long> getSeatPreemptScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("script/redis/seatPreempt.lua"));
        script.setResultType(Long.class);
        return script;
    }

    private List<String> createKeys(List<Long> seatIds, String loginId) {
        List<String> keys = new ArrayList<>();
        seatIds.forEach(seatId -> keys.add("seat:" + seatId));
        keys.add("user:booked:" + loginId);

        return keys;
    }
}
