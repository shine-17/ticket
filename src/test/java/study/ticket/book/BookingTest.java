package study.ticket.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.Task;
import study.ticket.application.service.BookingService;
import study.ticket.application.service.SeatService;
import study.ticket.domain.Booking;
import study.ticket.domain.Seat;
import study.ticket.domain.SeatState;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BookingTest {

    private static final int THREAD_COUNT = 5;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatService seatService;

    @Test
    @DisplayName("동시에 한 좌석을 예매한다.")
    void bookAtSameTime() throws InterruptedException {

        // given
        String[] ids = {"test1", "test2", "test3", "test4", "test5"};
        List<Long> seatIds = List.of(1L);

        Thread[] threads = new Thread[THREAD_COUNT];
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            threads[i] = new Thread(new Task(ids[i], seatIds, bookingService));
//        }
        threads[0] = new Thread(new Task("test1", List.of(1L, 2L), bookingService));
        threads[1] = new Thread(new Task("test2", List.of(2L, 3L), bookingService));
        threads[2] = new Thread(new Task("test3", List.of(1L, 3L), bookingService));
        threads[3] = new Thread(new Task("test4", List.of(1L, 2L), bookingService));
        threads[4] = new Thread(new Task("test5", List.of(1L), bookingService));

        // when
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        // then
        List<Booking> result = bookingService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("모든 예약 찾기")
    void findAllBooking() {

        // given

        // when
        List<Booking> result = bookingService.findAll();

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    @DisplayName("비관적 락 테스트")
    void pessimisticLockTest() {

        // given
        List<Long> seatIds = List.of(3L, 4L);

        // when
        seatService.updateToBooked(seatIds);

        // then
        List<Seat> seats = seatService.findByIds(seatIds);
        boolean result = seats.stream()
                .allMatch(Seat::available);
        assertThat(result).isFalse();
    }


    static class Task implements Runnable {
        private final String loginId;
        private final List<Long> seatIds;
        private final BookingService bookingService;

        public Task(String loginId, List<Long> seatIds, BookingService bookingService) {
            this.loginId = loginId;
            this.seatIds = seatIds;
            this.bookingService = bookingService;
        }

        @Override
        public void run() {
            bookingService.book(loginId, seatIds);
        }
    }

}
