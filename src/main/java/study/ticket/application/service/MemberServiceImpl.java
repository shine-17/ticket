package study.ticket.application.service;

import org.springframework.stereotype.Service;
import study.ticket.domain.Member;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public Optional<Member> findById(String loginId) {
        return Optional.empty();
    }
}
