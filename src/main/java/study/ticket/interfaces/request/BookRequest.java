package study.ticket.interfaces.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookRequest {
    private String id;
    private String loginId;
    private long showId;
    private List<Long> seatIds;
}
