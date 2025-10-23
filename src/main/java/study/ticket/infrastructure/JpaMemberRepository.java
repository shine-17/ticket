package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
}
