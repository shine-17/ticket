package study.ticket.infrastructure.command.seat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.ticket.domain.SeatState;

import java.util.List;

@Repository("jpaSeatRepository")
public class JpaSeatCommandRepository implements SeatCommandRepository {

    @PersistenceContext
    private EntityManager em;

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
