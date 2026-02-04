package study.ticket.infrastructure.query.booking;

import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingQueryRepository {
    Optional<BookingQuery> findById(long id);
    Optional<BookingQuery> findByMemberId(String loginId);
    List<BookingQuery> findByIds(List<Long> ids);
    List<BookingQuery> findAll();
}
