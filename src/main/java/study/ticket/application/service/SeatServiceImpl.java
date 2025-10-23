package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.SeatRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public Optional<Seat> findById(long id) {
        return seatRepository.findById(id);
    }

    @Override
    public List<Seat> findByIds(List<Long> seatIds) {
        return seatRepository.findByIds(seatIds);
    }

    @Override
    public void updateToBooked(List<Long> seatIds) {

    }
}
