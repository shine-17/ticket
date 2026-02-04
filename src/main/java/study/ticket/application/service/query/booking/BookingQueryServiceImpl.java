package study.ticket.application.service.query.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ticket.application.mapper.BookingMapper;
import study.ticket.application.port.BookingCachePort;
import study.ticket.domain.Booking;
import study.ticket.infrastructure.query.booking.BookingQueryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingQueryServiceImpl implements BookingQueryService {

    private final BookingQueryRepository bookingQueryRepository;
    private final BookingCachePort bookingCachePort;
    private BookingMapper mapper;

    @Override
    public Optional<BookingQuery> findById(long id) {
        return Optional.ofNullable(bookingCachePort.find(id)
//                .orElseGet(() -> bookingQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException("예약이 존재하지 않습니다."))));
                .orElseGet(() -> {
                    BookingQuery booking = bookingQueryRepository.findById(id).orElseThrow(() -> new IllegalStateException("예약이 존재하지 않습니다."));
                    bookingCachePort.save(booking);
                    return booking;
                }));
    }

    @Override
    public Optional<BookingQuery> findByMemberId(String loginId) {
        return bookingQueryRepository.findByMemberId(loginId);
    }

    @Override
    public List<BookingQuery> findByIds(List<Long> ids) {
        return bookingQueryRepository.findByIds(ids);
    }

    @Override
    public List<BookingQuery> findAll() {
        return bookingQueryRepository.findAll();
    }
}
