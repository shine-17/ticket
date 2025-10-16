package study.ticket.application.service;

import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatService {
    Optional<Seat> findById(long id);
    List<Seat> findByIds(List<Long> seatIds);
    void updateToBooked(List<Long> seatIds);
}
