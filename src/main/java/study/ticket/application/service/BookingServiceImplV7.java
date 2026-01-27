package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.domain.Show;
import study.ticket.infrastructure.BookingRepository;
import study.ticket.infrastructure.redis.seat.RedisKeys;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

//@Service
@RequiredArgsConstructor
@Slf4j
// version7: Redis
public class BookingServiceImplV7 implements BookingService {

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
        assertValidateBookedSeat(showId, seatIds);
        assertValidatePreemptedSeat(loginId, showId, seatIds);
    }

    private void assertValidateBookedSeat(long showId, List<Long> seatIds) {
        List<String> keys = RedisKeys.BOOKED.generateKeys(showId, seatIds);
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);

        assert values != null;

        for (Object value : values) {
            if (value != null) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }
        }
    }

    private void assertValidatePreemptedSeat(String loginId, long showId, List<Long> seatIds) {
        Map<String, String> keyMap = RedisKeys.PREEMPTED.generateKeyMap(loginId, showId, seatIds);

        Boolean result = redisTemplate.opsForValue()
                .multiSetIfAbsent(keyMap);

        if (result == null) {
            throw new IllegalStateException("좌석 선점 중 오류가 발생했습니다.");
        }
        else if (!result) {
            throw new IllegalStateException("이미 선점된 좌석입니다.");
        }

        keyMap.keySet().forEach(key -> redisTemplate.expire(key, SEAT_TTL, TimeUnit.SECONDS));
    }

//    private Map<String, String> getKeyMap(String loginId, long showId, List<Long> seatIds, String keyFormat) {
//        return generateKeys(keyFormat, showId, seatIds).stream()
//                .collect(Collectors.toMap(
//                        key -> key,
//                        value -> loginId + ":" + LocalDateTime.now()
//                ));
//    }

//    private List<String> generateKeys(String keyFormat, long showId, List<Long> seatIds) {
//        List<String> keys = new ArrayList<>();
//        seatIds.forEach(seatId -> keys.add(String.format(keyFormat, showId, seatId)));
//        return keys;
//    }

    private void cacheBookedSeat(String loginId, long showId, List<Long> seatIds) {
        Map<String, String> keyMap = RedisKeys.BOOKED.generateKeyMap(loginId, showId, seatIds);

        Boolean result = redisTemplate.opsForValue()
                .multiSetIfAbsent(keyMap);

        if (result == null) {
            throw new IllegalStateException("예매 중 오류가 발생했습니다.");
        }

        // 예약 좌석 TTL 캐시
        keyMap.keySet().forEach(bookedSeat -> redisTemplate.expire(bookedSeat, BOOKED_TTL, TimeUnit.SECONDS));

        // 선점 좌석 TTL (이 메서드가 원자적이지 않기 때문에 예약 좌석을 캐싱할 때 다른 트랜잭션이 통과해서 선점 시도하려고 할 수 있기 때문에 TTL 설정으로 방지
        Map<String, String> preemptedKeyMap = RedisKeys.PREEMPTED.generateKeyMap(loginId, showId, seatIds);
        preemptedKeyMap.keySet().forEach(preemptedSeat -> redisTemplate.expire(preemptedSeat, 10, TimeUnit.SECONDS));
    }
}
