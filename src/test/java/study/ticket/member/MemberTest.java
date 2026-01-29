package study.ticket.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ticket.application.service.command.member.MemberCommandService;
import study.ticket.application.service.query.member.MemberQueryService;
import study.ticket.domain.Member;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @Test
    @DisplayName("loginId로 회원 찾기")
    void findByLoginIdTest() {
        Member member = memberQueryService.findByLoginId("test1").orElse(null);
        System.out.println(member);

        assertThat(member).isNotNull();
    }
}
