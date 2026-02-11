package study.ticket.application.port;

import study.ticket.application.service.query.booking.BookingQuery;

import java.util.Optional;

public interface BookingCachePort {
    Optional<BookingQuery> find(long id);
    BookingQuery save(BookingQuery booking);
}
