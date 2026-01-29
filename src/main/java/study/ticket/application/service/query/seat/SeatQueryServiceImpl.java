package study.ticket.application.service.query.seat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.command.seat.SeatCommandRepository;
import study.ticket.infrastructure.query.seat.SeatQueryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatQueryServiceImpl implements SeatQueryService {

    private final SeatQueryRepository seatQueryRepository;

    @Override
    public Optional<Seat> findById(long id) {
        return seatQueryRepository.findById(id);
    }

    @Override
    public List<Seat> findByIds(List<Long> seatIds) {
        return seatQueryRepository.findByIds(seatIds);
    }

}
