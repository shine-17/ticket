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
        // 1. 각 공연 별 좌석 데이터를 먼저 테이블에 삽입
        // 2. 해당 데이터에 대한 lock

        // 테이블 구조 변경 필요
        // - 공연 별 좌석 데이터, 지정된 좌석 데이터
    }
}
