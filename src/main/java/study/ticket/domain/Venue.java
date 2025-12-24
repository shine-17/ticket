package study.ticket.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "venue")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    @Id
    private long id;

    private String name;

}
