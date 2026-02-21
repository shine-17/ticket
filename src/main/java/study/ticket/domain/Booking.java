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
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "show_id", referencedColumnName = "id")
    private Show show;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;

    public static Booking of(Member member, Show show, Seat seat) {
        return new Booking(member, show, seat);
    }

    public static List<Booking> of(Member member, Show show, List<Seat> seats) {
        List<Booking> bookings = new ArrayList<>();

        for (Seat seat : seats) {
            bookings.add(new Booking(member, show, seat));
        }

        return bookings;
    }

    private Booking(Member member, Show show, Seat seat) {
        this.member = member;
        this.show = show;
        this.seat = seat;
    }
}
