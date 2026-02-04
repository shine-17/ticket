package study.ticket.infrastructure.query.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.ticket.application.mapper.BookingMapper;
import study.ticket.application.service.query.booking.BookingQuery;
import study.ticket.domain.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBookingQueryRepository implements BookingQueryRepository {

    @PersistenceContext
    private EntityManager em;

    private final BookingMapper mapper;

    @Override
    public Optional<BookingQuery> findById(long id) {
        return Optional.ofNullable(
                mapper.toBookingQuery(em.find(Booking.class, id))
        );
    }

    @Override
    public Optional<BookingQuery> findByMemberId(String loginId) {
        return Optional.ofNullable(
                mapper.toBookingQuery(em.find(Booking.class, loginId))
        );
    }

    @Override
    public List<BookingQuery> findByIds(List<Long> ids) {
        List<BookingQuery> bookings = new ArrayList<>();

        for (Long id : ids) {
            bookings.add(mapper.toBookingQuery(em.find(Booking.class, id)));
        }

        return bookings;
    }

    @Override
    public List<BookingQuery> findAll() {
        return em.createQuery("SELECT b FROM booking b", Booking.class).getResultList()
                .stream()
                .map(mapper::toBookingQuery)
                .toList();
    }

}
