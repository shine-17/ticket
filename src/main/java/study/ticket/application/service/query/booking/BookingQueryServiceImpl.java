package study.ticket.application.service.query.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ticket.domain.Booking;
import study.ticket.infrastructure.query.booking.BookingQueryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingQueryRepository bookingQueryRepository;

    @Override
    public Optional<Booking> findById(long id) {
        return bookingQueryRepository.findById(id);
    }

    @Override
    public Optional<Booking> findByMemberId(String loginId) {
        return bookingQueryRepository.findByMemberId(loginId);
    }

    @Override
    public List<Booking> findByIds(List<Long> ids) {
        return bookingQueryRepository.findByIds(ids);
    }

    @Override
    public List<Booking> findAll() {
        return bookingQueryRepository.findAll();
    }
}
