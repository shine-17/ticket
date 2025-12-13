package study.ticket.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ticket.application.service.BookingService;
import study.ticket.domain.Booking;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BookingTest {

    private static final int THREAD_COUNT = 5;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("동시에 한 좌석을 예매한다.")
    void bookAtSameTime() throws InterruptedException {

        // given
        String[] ids = {"test1", "test2", "test3", "test4", "test5"};
        List<Long> seatIds = List.of(1L);

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new Task(ids[i], seatIds, bookingService));
        }

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
