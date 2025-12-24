package study.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "show")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    private long id;

    private String name;

    private long venue_id;

    private LocalDate date;

    private int round;

    private int duration;

    private long price;

}
