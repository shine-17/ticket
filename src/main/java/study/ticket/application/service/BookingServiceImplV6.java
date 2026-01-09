package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
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

    private static final int MAX_SEAT_COUNT = 2;

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
//        int bookedSeatCount = Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(loginId)));
//        int userSeatCount = bookedSeatCount + seatIds.size();
//
//        if (userSeatCount > MAX_SEAT_COUNT) {
//            throw new IllegalStateException("1인 최대 2매까지 예매 가능합니다.");
//        }
//
//        Map<String, String> map = seatIds.stream()
//                .collect(Collectors.toMap(
//                        String::valueOf,
//                        seatId -> loginId + ":" + LocalDateTime.now()
//                ));
//        map.put(loginId, String.valueOf(userSeatCount));
//
//        Boolean result = redisTemplate.opsForValue().multiSetIfAbsent(map);
//        if (!Boolean.TRUE.equals(result)) {
//            if (result == null) {
//                // 통신 오류
//            }
//
//            throw new IllegalStateException("이미 예약된 좌석입니다.");
//        }
//
//        seatIds.forEach(seatId -> redisTemplate.expire(String.valueOf(seatId), 1, TimeUnit.MINUTES));

        // Lua Script
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("script/redis/seatPreempt.lua"));
        script.setResultType(Long.class);

        List<String> keys = new ArrayList<>();
        seatIds.forEach(seatId -> keys.add("seat:" + seatId));
        keys.add("user:booked:" + loginId);

        Long result = redisTemplate.execute(
                script,
                keys,
                loginId,
                seatIds.size(),
                MAX_SEAT_COUNT,
                60
        );

        System.out.println("result = " + result);
    }
}
