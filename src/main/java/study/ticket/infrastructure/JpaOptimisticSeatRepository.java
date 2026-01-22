package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Seat;
import study.ticket.domain.SeatState;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaOptimisticSeatRepository implements SeatRepository {

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
    @Lock(LockModeType.OPTIMISTIC)
    public void updateToBookedOrThrow(long showId, List<Long> seatIds) {
        /*
            1. 각 공연 별 좌석 데이터를 먼저 테이블에 삽입
            2. 해당 데이터에 대한 lock

            테이블 구조 변경 필요
            - 공연 별 좌석 데이터, 지정된 좌석 데이터

            장소(공연장), 장소에 맞는 좌석 정보
            회차별 공연 좌석 정보

            공연장 테이블 venue
            공연 테이블 (공연명, 공연장, 날짜, 회차, 목적? ..) show
            좌석 테이블 (공연 테이블과 1:N, 공연장 좌석 정보)

            Q. 공연장 별 좌석수를 공연 테이블에 컬럼으로 넣기 vs 쿼리로 공연장 별 좌석수를 카운팅하기
         */

        List<Seat> seats = em.createQuery("SELECT s FROM seat s WHERE s.show_id = :showId AND s.state = :available AND s.id IN :seatIds", Seat.class)
                .setParameter("showId", showId)
                .setParameter("available", SeatState.AVAILABLE.getState())
                .setParameter("seatIds", seatIds)
                .getResultList();

        seats.forEach(Seat::preempt);
    }
}
