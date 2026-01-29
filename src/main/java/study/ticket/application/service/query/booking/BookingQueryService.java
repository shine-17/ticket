package study.ticket.application.service.query.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    Optional<Booking> findById(long id);
    Optional<Booking> findByMemberId(String loginId);
//    Optional<Booking> findByMemberIdAndShowId(String loginId);
    List<Booking> findByIds(List<Long> ids);
    List<Booking> findAll();

    Logger log = LoggerFactory.getLogger(BookingQueryService.class);

    default void pay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
