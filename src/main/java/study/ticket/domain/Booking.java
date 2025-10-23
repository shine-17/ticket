package study.ticket.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "login_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;

    public static Booking of(Member member, Seat seat) {
        return new Booking(member, seat);
    }

    public static List<Booking> of(Member member, List<Seat> seats) {
        if (seats.size() > 2) throw new IllegalStateException("1인 최대 2매까지 예매 가능합니다.");

        List<Booking> bookings = new ArrayList<>();
        for (Seat seat : seats) {
            bookings.add(new Booking(member, seat));
        }

        return bookings;
    }

    private Booking(Member member, Seat seat) {
        this.member = member;
        this.seat = seat;
    }
}
