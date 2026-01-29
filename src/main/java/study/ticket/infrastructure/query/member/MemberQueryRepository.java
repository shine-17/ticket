package study.ticket.infrastructure.query.member;

import study.ticket.domain.Member;

import java.util.Optional;

public interface MemberQueryRepository {
    Optional<Member> findByLoginId(String loginId);
}
