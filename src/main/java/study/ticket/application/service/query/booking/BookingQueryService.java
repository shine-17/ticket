package study.ticket.application.service.query.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.ticket.domain.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingQueryService {
    Optional<BookingQuery> findById(long id);
    Optional<BookingQuery> findByMemberId(String loginId);
//    Optional<Booking> findByMemberIdAndShowId(String loginId);
    List<BookingQuery> findByIds(List<Long> ids);
    List<BookingQuery> findAll();

    Logger log = LoggerFactory.getLogger(BookingQueryService.class);

    default void pay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
