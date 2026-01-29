package study.ticket.application.service.command.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.ticket.domain.Booking;

import java.util.List;

public interface BookingCommandService {
    void book(String loginId, long showId, List<Long> seatIds);
    void save(Booking booking);
    void save(List<Booking> bookings);

    Logger log = LoggerFactory.getLogger(BookingCommandService.class);

    default void pay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
