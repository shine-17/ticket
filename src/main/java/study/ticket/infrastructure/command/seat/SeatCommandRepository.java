package study.ticket.infrastructure.command.seat;

import java.util.List;

public interface SeatCommandRepository {
    void updateToBookedOrThrow(long showId, List<Long> seatIds);
}
