package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Seat;
import study.ticket.domain.SeatState;

import java.util.List;
import java.util.Optional;

//@Repository
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
    public void updateToBookedOrThrow(long showId, List<Long> seatIds) {
        int result = em.createQuery("UPDATE seat s SET s.state = :state WHERE s.show_id = :showId AND s.state = :available AND s.id IN :seatIds")
                .setParameter("showId", showId)
                .setParameter("available", SeatState.AVAILABLE.getState())
                .setParameter("state", SeatState.PREEMPT.getState())
                .setParameter("seatIds", seatIds)
                .executeUpdate();

        if (result != seatIds.size()) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
    }
}
