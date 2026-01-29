package study.ticket.application.service.query.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ticket.domain.Member;
import study.ticket.infrastructure.command.member.MemberCommandRepository;
import study.ticket.infrastructure.query.member.MemberQueryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberQueryRepository.findByLoginId(loginId);
    }

}
