package study.ticket.application.service;

import study.ticket.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findByLoginId(String loginId);
    void increaseBookingCount(String loginId, long showId, int seatCount, int compareCount);
}
