package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.BookingRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

//@Service
@RequiredArgsConstructor
@Slf4j
// version7: Redisson
public class BookingServiceImplV7 implements BookingService {

    private final MemberService memberService;
    private final SeatService seatService;
    private final BookingRepository bookingRepository;

//    private final StringRedisTemplate redisTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

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

        RLock[] rLocks = seatIds.stream()
                .map(seatId -> redissonClient.getLock(String.valueOf(seatId)))
                .toArray(RLock[]::new);

        RLock multiLock = redissonClient.getMultiLock(rLocks);

        try {
            boolean result = multiLock.tryLock(5, TimeUnit.SECONDS);

            if (!result) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }

            seatIds.forEach(seatId -> redisTemplate.opsForValue().set(String.valueOf(seatId), loginId, 10, TimeUnit.SECONDS));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            multiLock.unlock();
        }
    }
}
