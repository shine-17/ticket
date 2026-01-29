package study.ticket.infrastructure.command.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;

import java.util.List;

@Repository
public class JpaBookingCommandRepository implements BookingCommandRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Booking booking) {
        em.persist(booking);
    }

    @Override
    @Transactional
    public void save(List<Booking> bookings) {
        for (Booking booking : bookings) {
            em.persist(booking);
        }
    }
}
