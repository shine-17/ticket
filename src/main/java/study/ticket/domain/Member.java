package study.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @NotNull @NotEmpty @Positive
    private long id;

    @NotNull @NotEmpty
    private String loginId; // 로그인 ID

    @NotNull @NotEmpty
    private String name; // 사용자 이름

    @NotNull @NotEmpty
    private String password;
}
