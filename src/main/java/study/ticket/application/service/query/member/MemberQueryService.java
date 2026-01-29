package study.ticket.application.service.query.member;

import study.ticket.domain.Member;

import java.util.Optional;

public interface MemberQueryService {
    Optional<Member> findByLoginId(String loginId);
}
