package study.ticket.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<Booking> findById(String id);
    Optional<Booking> findByMemberId(String loginId);
    List<Booking> findByIds(List<Long> ids);
    List<Booking> findAll();
    void book(String loginId, List<Long> seatIds);
    void save(Booking booking);
    void save(List<Booking> bookings);

    Logger log = LoggerFactory.getLogger(BookingService.class);

    default void pay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
