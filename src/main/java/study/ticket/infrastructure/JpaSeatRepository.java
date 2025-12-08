package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Seat;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaSeatRepository implements SeatRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Seat> findById(long id) {
        return Optional.ofNullable(em.find(Seat.class, id));
    }

    @Override
    public List<Seat> findByIds(List<Long> seatIds) {
        return em.createQuery("SELECT s FROM seat s WHERE s.id IN :seatIds", Seat.class)
                .setParameter("seatIds", seatIds)
                .getResultList();
    }

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE) // lock timeout
    public void updateToBooked(List<Long> seatIds) {

    }
}
