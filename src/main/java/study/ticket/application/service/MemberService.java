package study.ticket.application.service;

import study.ticket.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(String loginId);
}
