package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Member;

import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return Optional.ofNullable(
                em.createQuery("SELECT m FROM member m WHERE m.loginId = :loginId", Member.class)
                        .setParameter("loginId", loginId)
                        .getSingleResult());
    }

    @Override
    public int increaseBookingCount(String loginId, long showId, int seatCount, int compareCount) {

//        String sql =
//                "MERGE INTO member_booking_counter AS mc " +
//                        "USING (SELECT id, :showId AS show_id FROM member WHERE login_id = :loginId) AS m " +
//                        "ON mc.member_id = m.id AND mc.show_id = m.show_id " +
//                        "WHEN NOT MATCHED THEN " +
//                        "    INSERT (member_id, show_id, count) " +
//                        "    VALUES (m.id, m.show_id, :count) " +
//                        "WHEN MATCHED AND (mc.count + :count <= :compare) THEN " +
//                        "    UPDATE SET count = mc.count + :count";

        String sql =
                "INSERT INTO member_booking_counter (member_id, show_id, count) " +
                        "SELECT m.id, :showId, :count " +
                        "FROM member m " +
                        "WHERE m.login_id = :loginId " +
                        "ON CONFLICT (member_id, show_id) " +
                        "DO UPDATE SET count = member_booking_counter.count + :count " +
                        "WHERE member_booking_counter.count + :count <= :compare;";

        return em.createNativeQuery(sql)
                .setParameter("loginId", loginId)
                .setParameter("showId", showId)
                .setParameter("count", seatCount)
                .setParameter("compare", compareCount)
                .executeUpdate();
    }
}
