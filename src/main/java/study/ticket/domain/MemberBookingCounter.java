package study.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member_booking_counter")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberBookingCounter {
    @Id
    private Long id;

    private long member_id;
    private long show_id;
    private int count;
}
