package study.ticket.application.service.command.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.ticket.infrastructure.command.member.MemberCommandRepository;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberCommandRepository memberRepository;

    @Override
    @Transactional
    public void increaseBookingCount(String loginId, long showId, int seatCount, int compareCount) {
        int updated = memberRepository.increaseBookingCount(loginId, showId, seatCount, compareCount);
        if (updated == 0) {
            throw new IllegalStateException("1인 최대 " + compareCount + "매까지 예매 가능합니다.");
        }
    }
}
