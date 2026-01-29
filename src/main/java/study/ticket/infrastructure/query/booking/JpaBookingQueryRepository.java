package study.ticket.infrastructure.query.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookingQueryRepository implements BookingQueryRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Booking> findById(long id) {
        return Optional.ofNullable(em.find(Booking.class, id));
    }

    @Override
    public Optional<Booking> findByMemberId(String loginId) {
        return Optional.ofNullable(em.find(Booking.class, loginId));
    }

    @Override
    public List<Booking> findByIds(List<Long> ids) {
        List<Booking> bookings = new ArrayList<>();

        for (Long id : ids) {
            bookings.add(em.find(Booking.class, id));
        }

        return bookings;
    }

    @Override
    public List<Booking> findAll() {
        return em.createQuery("SELECT b FROM booking b", Booking.class).getResultList();
    }

}
