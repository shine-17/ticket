package study.ticket.infrastructure;

import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(long id);
    List<Seat> findByIds(List<Long> seatIds);
    void updateToBooked(List<Long> seatIds);
}
