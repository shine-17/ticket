package study.ticket.infrastructure;

import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Optional<Booking> findById(String id);
    Optional<Booking> findByMemberId(String loginId);
    List<Booking> findByIds(List<Long> ids);
    void save(Booking booking);
    void save(List<Booking> bookings);
}
