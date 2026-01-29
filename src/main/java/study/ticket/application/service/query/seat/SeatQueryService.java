package study.ticket.application.service.query.seat;

import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatQueryService {
    Optional<Seat> findById(long id);
    List<Seat> findByIds(List<Long> seatIds);
}
