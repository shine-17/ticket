package study.ticket.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "seat")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    private long id;

    @NotNull @NotEmpty
    private String zone;

    @Positive
    private int row;

    @Positive
    private int number;

    /*
        A(Available 예약 가능)
                H(Held 결제 대기중)
                O(On Hold 임시 선점 중)
        P(Preempt 선점 중)
        S(Sold,Occupied 판매 완료, 예약 확정)
        B(Blocked,Reserved 판매 불가)
        R(Refunded/Returned 환불된 좌석, 재판매 가능 여부 결정 전 임시 상태)
     */

    private char state;

    private long show_id;

//    @Version
//    private Long version;

    public boolean available() {
        return state == 'A';
    }

    public void preempt() {
        state = 'P';
    }

}
