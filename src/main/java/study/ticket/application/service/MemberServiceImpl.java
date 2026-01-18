package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.domain.Member;
import study.ticket.infrastructure.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    @Override
    @Transactional
    public int increaseBookingCount(String loginId, long showId, int seatCount, int compareCount) {
        return memberRepository.increaseBookingCount(loginId, showId, seatCount, compareCount);
    }
}
