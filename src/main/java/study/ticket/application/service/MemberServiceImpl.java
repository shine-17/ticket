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
    public void increaseBookingCount(String loginId, long showId, int seatCount, int compareCount) {
        int updated = memberRepository.increaseBookingCount(loginId, showId, seatCount, compareCount);
        if (updated == 0) {
            throw new IllegalStateException("1인 최대 " + compareCount + "매까지 예매 가능합니다.");
        }
    }
}
