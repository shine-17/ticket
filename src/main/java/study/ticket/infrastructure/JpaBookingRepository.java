package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookingRepository implements BookingRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Booking> findById(String id) {
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
