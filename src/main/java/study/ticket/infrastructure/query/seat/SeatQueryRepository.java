package study.ticket.infrastructure.query.seat;

import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatQueryRepository {
    Optional<Seat> findById(long id);
    List<Seat> findByIds(List<Long> seatIds);
}
