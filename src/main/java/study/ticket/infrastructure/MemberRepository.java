package study.ticket.infrastructure;

import study.ticket.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByLoginId(String loginId);
}
