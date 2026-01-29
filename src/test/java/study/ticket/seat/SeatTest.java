package study.ticket.seat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ticket.application.service.command.seat.SeatCommandService;
import study.ticket.application.service.query.seat.SeatQueryService;
import study.ticket.domain.Seat;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class SeatTest {

    @Autowired
    private SeatQueryService seatQueryService;

    @Test
    @DisplayName("번호로 좌석 찾기")
    void findById() {
        long seatId = 1L;
        Seat seat = seatQueryService.findById(seatId).orElse(null);
        assertThat(seat).isNotNull();
    }

    @Test
    @DisplayName("번호로 여러 좌석 찾기")
    void findByIds() {
        List<Long> seatIds = List.of(1L, 2L);
        List<Seat> seats = seatQueryService.findByIds(seatIds);
        assertThat(seats).hasSize(seatIds.size());
    }

}
