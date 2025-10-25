package study.ticket.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ticket.application.service.MemberService;
import study.ticket.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("loginId로 회원 찾기")
    void findByLoginIdTest() {
        Member member = memberService.findByLoginId("test1").orElse(null);
        System.out.println(member);
        assertThat(member).isNotNull();
    }
}
