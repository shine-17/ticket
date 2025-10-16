package study.ticket.member;

import org.junit.jupiter.api.Test;
import study.ticket.domain.Member;
import study.ticket.domain.Seat;

public class MemberTest {

    @Test
    void createMember() {
        Member member = new Member(1L, "shine", "도훈", "1234");


    }
}
