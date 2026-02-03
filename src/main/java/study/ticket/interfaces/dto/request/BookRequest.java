package study.ticket.interfaces.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BookRequest {
    private long id;
    private String loginId;
    private long showId;
    private List<Long> seatIds;
}
