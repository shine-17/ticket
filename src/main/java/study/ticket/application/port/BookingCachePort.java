package study.ticket.application.port;

import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.domain.Booking;

import java.util.Optional;

public interface BookingCachePort {
    Optional<BookingQuery> find(long id);
    void save(BookingQuery booking);
}
