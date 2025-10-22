package study.ticket.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.ticket.application.service.BookingService;
import study.ticket.domain.Booking;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class BookingTest {

    private static final int THREAD_COUNT = 5;

    @Autowired
    private BookingService bookingService;

    @Test
    @DisplayName("동시에 한 좌석을 예매한다.")
    void bookAtSameTime() {

        // given
        String[] ids = {"test1", "test2", "test3", "test4", "test5"};

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {

            });
        }

        // when

        // then


    }

}
