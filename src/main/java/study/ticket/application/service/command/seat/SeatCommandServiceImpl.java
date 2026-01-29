package study.ticket.application.service.command.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.infrastructure.command.seat.SeatCommandRepository;

import java.util.List;

//@Service
@RequiredArgsConstructor
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatCommandRepository seatCommandRepository;

    @Override
    @Transactional
    public void updateToBooked(long showId, List<Long> seatIds) {
        seatCommandRepository.updateToBookedOrThrow(showId, seatIds);
    }
}
