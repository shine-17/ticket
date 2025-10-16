package study.ticket.application.service;

import org.springframework.stereotype.Service;
import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

@Service
public class SeatServiceImpl implements SeatService {

    @Override
    public Optional<Seat> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Seat> findByIds(List<Long> seatIds) {
        return List.of();
    }

    @Override
    public void updateToBooked(List<Long> seatIds) {

    }
}
