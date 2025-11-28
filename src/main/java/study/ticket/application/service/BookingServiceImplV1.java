package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.BookingRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImplV1 implements BookingService {

    private final MemberService memberService;
    private final SeatService seatService;
    private final BookingRepository bookingRepository;

    private final Set<Long> seatQueue = new HashSet<>();
//    private final Set<Long> bookingQueue = ConcurrentHashMap.newKeySet();

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
        validateSeat(seatIds);

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

    private synchronized void validateSeat(List<Long> seatIds) {
        for (Long seatId : seatIds) {
            if (seatQueue.contains(seatId)) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }

            seatQueue.add(seatId);
        }
    }

    private void pay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

}
