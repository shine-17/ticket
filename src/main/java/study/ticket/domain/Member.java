package study.ticket.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id @NotNull @NotEmpty @Positive
    private long id;

    @NotNull @NotEmpty
    @Column(name = "login_id")
    private String loginId; // 로그인 ID

    @NotNull @NotEmpty
    private String name; // 사용자 이름

    @NotNull @NotEmpty
    private String password;
}
