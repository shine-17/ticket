package study.ticket.application.service.command.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.application.service.command.member.MemberCommandService;
import study.ticket.application.service.command.seat.SeatCommandService;
import study.ticket.application.service.command.show.ShowCommandService;
import study.ticket.application.service.query.member.MemberQueryService;
import study.ticket.application.service.query.seat.SeatQueryService;
import study.ticket.application.service.query.show.ShowQueryService;
import study.ticket.domain.Booking;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;
import study.ticket.domain.Show;
import study.ticket.infrastructure.command.booking.BookingCommandRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//@Service
@RequiredArgsConstructor
@Slf4j
// version2: ReentrantLock(nonfair)
public class BookingCommandServiceImplV3 implements BookingCommandService {

    private final SeatCommandService seatCommandService;

    private final MemberQueryService memberQueryService;
    private final SeatQueryService seatQueryService;
    private final ShowQueryService showQueryService;

    private final BookingCommandRepository bookingCommandRepository;

    private final Set<Long> seatQueue = new HashSet<>();

    // 비공정 모드 (성능 DOWN)
    private final Lock lock = new ReentrantLock();

    @Override
    @Transactional
    public void book(String loginId, long showId, List<Long> seatIds) {
        Member member = memberQueryService.findByLoginId(loginId).orElseThrow(() -> new IllegalStateException("아이디를 찾을 수 없습니다"));
        List<Seat> seats = seatQueryService.findByIds(seatIds);
        Show show = showQueryService.findById(showId).orElseThrow(() -> new IllegalStateException("공연을 찾을 수 없습니다"));

        // 좌석 선점 확인
        assertValidateSeat(seatIds);

        // 좌석 선점 (좌석 상태 변경) - 동시성 문제 발생
        seatCommandService.updateToBooked(showId, seatIds);

        // 한 명의 회원은 최대 2매까지 예매가능
        List<Booking> bookings = Booking.of(member, show, seats);

        // 결제
//        pay();

        // 예매 정보 등록
        save(bookings);
    }

    @Override
    public void save(Booking booking) {
        bookingCommandRepository.save(booking);
    }

    @Override
    public void save(List<Booking> bookings) {
        bookingCommandRepository.save(bookings);
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
