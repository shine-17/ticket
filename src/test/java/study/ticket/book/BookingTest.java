package study.ticket.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import study.ticket.application.service.command.booking.BookingCommandService;
import study.ticket.application.service.command.member.MemberCommandService;
import study.ticket.application.service.command.seat.SeatCommandService;
import study.ticket.application.service.command.show.ShowCommandService;
import study.ticket.application.service.query.booking.BookingQueryService;
import study.ticket.application.service.query.seat.SeatQueryService;
import study.ticket.domain.Booking;
import study.ticket.domain.Seat;
import study.ticket.infrastructure.command.booking.BookingCommandRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class BookingTest {

    private static final int THREAD_COUNT = 5;

    @Autowired @Qualifier("seatServiceWithJpa")
    SeatCommandService seatCommandService;
    @Autowired @Qualifier("bookingServiceWithRedis")
    BookingCommandService bookingCommandService;

    @Autowired
    BookingQueryService bookingQueryService;
    @Autowired
    SeatQueryService seatQueryService;

    @Autowired
    private BookingCommandRepository bookingCommandRepository;

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private ShowCommandService showCommandService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedissonClient redisson;

    @BeforeEach
    void before() {
//        redisTemplate.opsForValue().getOperations().delete(List.of("1", "2", "3"));

//        BookingServiceConfig bookingServiceConfig = new BookingServiceConfig();
//        SeatServiceConfig seatServiceConfig = new SeatServiceConfig();
//        seatService = seatServiceConfig.seatServiceWithPessimistic();
//        bookingService = bookingServiceConfig.bookingServiceV4(seatService, bookingRepository, memberService, showService);

    }

    @Test
    @DisplayName("동시에 한 좌석을 예매한다.")
    void bookAtSameTime() throws InterruptedException {

        // given
        String[] ids = {"test1", "test2", "test3", "test4", "test5"};
        List<Long> seatIds = List.of(1L);

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new Task(ids[i], seatIds, bookingCommandService));
        }

//        threads[0] = new Thread(new Task("test1", List.of(1L, 2L), bookingService));
//        threads[1] = new Thread(new Task("test2", List.of(2L, 3L), bookingService));
//        threads[2] = new Thread(new Task("test3", List.of(1L, 3L), bookingService));
//        threads[3] = new Thread(new Task("test4", List.of(1L, 2L), bookingService));
//        threads[4] = new Thread(new Task("test5", List.of(1L), bookingService));

//        threads[0] = new Thread(new Task("test1", List.of(1L, 2L), bookingService));
//        threads[1] = new Thread(new Task("test2", List.of(1L, 2L), bookingService));
//        threads[2] = new Thread(new Task("test3", List.of(1L, 2L), bookingService));
//        threads[3] = new Thread(new Task("test4", List.of(2L, 3L), bookingService));
//        threads[4] = new Thread(new Task("test5", List.of(2L, 3L), bookingService));

        // when
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        // then
        List<Booking> result = bookingQueryService.findAll();
        assertThat(result).hasSize(1);
//        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("모든 예약 찾기")
    void findAllBooking() {

        // given

        // when
        List<Booking> result = bookingQueryService.findAll();

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    @DisplayName("비관적 락 테스트")
    void pessimisticLockTest() {

        // given
        List<Long> seatIds = List.of(3L, 4L);
        long showId = 1;

        // when
        seatCommandService.updateToBooked(showId, seatIds);

        // then
        List<Seat> seats = seatQueryService.findByIds(seatIds);
        boolean result = seats.stream()
                .allMatch(Seat::available);
        assertThat(result).isFalse();
    }

    static class Task implements Runnable {
        private final String loginId;
        private final List<Long> seatIds;
        private final BookingCommandService bookingCommandService;

        public Task(String loginId, List<Long> seatIds, BookingCommandService bookingCommandService) {
            this.loginId = loginId;
            this.seatIds = seatIds;
            this.bookingCommandService = bookingCommandService;
        }

        @Override
        public void run() {
            bookingCommandService.book(loginId, 1, seatIds);
        }
    }

    @Test
    void redisLockTest1() throws InterruptedException {
        String key = "seat:1";

        Runnable runnable = () -> {
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(key, Thread.currentThread().getName(), 30, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + ": " + result);
        };

        concurrentTestTemplate(runnable);
    }

    @Test
    void redisLockTest2() throws InterruptedException {
        List<Long> seatIds = List.of(1L, 2L);
        String loginId = "test1";

        Runnable runnable = () -> {
//                RLock[] rLocks = seatIds.stream()
//                        .map(seatId -> redisson.getLock(String.valueOf(seatId)))
//                        .toArray(RLock[]::new);
//
//                RLock multiLock = redisson.getMultiLock(rLocks);

            List<RLock> rLocks = seatIds.stream()
                    .map(seatId -> redisson.getLock(String.valueOf(seatId)))
                    .collect(Collectors.toList());
            rLocks.add(redisson.getLock(loginId));

            RLock multiLock = redisson.getMultiLock(rLocks.toArray(RLock[]::new));

            try {
                boolean result = multiLock.tryLock(3, 30, TimeUnit.SECONDS);

                if (!result) {
                    throw new IllegalStateException("이미 예약된 좌석입니다.");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//                finally {
//                    multiLock.unlock();
//                }
        };

        concurrentTestTemplate(runnable);
    }

    @Test
    void redisLockTest3() throws InterruptedException {

        String loginId = "test1";
        long showId = 1;
        int seatCount = 1;
        int compareCount = 2;

        Runnable runnable = () -> {
            try {
                memberCommandService.increaseBookingCount(loginId, showId, seatCount, compareCount);
                System.out.println(Thread.currentThread().getName() + ": OK");
            } catch (IllegalStateException e) {
                System.out.println(Thread.currentThread().getName() + ": FAIL - " + e.getMessage());
            }
        };

        concurrentTestTemplate(runnable);
    }

    @Test
    void redisLockTest4() {
        String key1 = "seat:booked:1";
        String key2 = "seat:booked:2";

        List<Object> values = redisTemplate.opsForValue().multiGet(List.of(key2));

        System.out.println(values);
        System.out.println("list size: " + values.size());

        values.forEach(value -> {
            if (value == null) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }
        });
    }

    void concurrentTestTemplate(Runnable runnable) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(runnable);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

}
