package study.ticket.application.service.command.seat;

import java.util.List;

public interface SeatCommandService {
    void updateToBooked(long showId, List<Long> seatIds);
}
