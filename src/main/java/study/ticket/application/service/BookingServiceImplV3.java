package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.BookingRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImplV3 implements BookingService {

    private final MemberService memberService;
    private final SeatService seatService;
    private final BookingRepository bookingRepository;

    private final Set<Long> seatQueue = new HashSet<>();

    // 비공정 모드 (성능 DOWN)
    private final Lock lock = new ReentrantLock();

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
        assertValidateSeat(seatIds);

        // 좌석 선점 (좌석 상태 변경) - 동시성 문제 발생
        seatService.updateToBooked(seatIds);

        // 한 명의 회원은 최대 2매까지 예매가능
        List<Booking> bookings = Booking.of(member, seats);

        // 결제
//        pay();

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

    private void assertValidateSeat(List<Long> seatIds) {

        // 대기 중인 예약을 타임아웃을 두면 안될 것 같고
        // 그럼 무한 대기를 어떻게 방지하지?
        lock.lock();

        try {
            for (Long seatId : seatIds) {
                if (seatQueue.contains(seatId)) {
                    throw new IllegalStateException("이미 예약된 좌석입니다.");
                }

                seatQueue.add(seatId);
            }
        } finally {
            lock.unlock();
        }
    }
}
