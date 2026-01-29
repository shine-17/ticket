package study.ticket.infrastructure.query.booking;

import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingQueryRepository {
    Optional<Booking> findById(long id);
    Optional<Booking> findByMemberId(String loginId);
    List<Booking> findByIds(List<Long> ids);
    List<Booking> findAll();
}
