package study.ticket.application.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import study.ticket.application.service.command.booking.*;
import study.ticket.application.service.command.member.MemberCommandService;
import study.ticket.application.service.command.seat.SeatCommandService;
import study.ticket.application.service.query.member.MemberQueryService;
import study.ticket.application.service.query.seat.SeatQueryService;
import study.ticket.application.service.query.show.ShowQueryService;
import study.ticket.infrastructure.command.booking.BookingCommandRepository;

@Configuration
public class BookingServiceConfig {

    @Bean("bookingServiceWithSynchronized")
    public BookingCommandService bookingServiceV1(
            @Qualifier("seatServiceWithJpa") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository
    ) {
        return new BookingCommandServiceImplV1(seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository);
    }

    @Bean("bookingServiceWithReentrantLockFair")
    public BookingCommandService bookingServiceV2(
            @Qualifier("seatServiceWithJpa") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository
    ) {
        return new BookingCommandServiceImplV2(seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository);
    }

    @Bean("bookingServiceWithReentrantLockNonFair")
    public BookingCommandService bookingServiceV3(
            @Qualifier("seatServiceWithJpa") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository
    ) {
        return new BookingCommandServiceImplV3(seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository);
    }

    @Bean("bookingServiceWithPessimistic")
    public BookingCommandService bookingServiceV4(
            @Qualifier("seatServiceWithPessimistic") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository
    ) {
        return new BookingCommandServiceImplV4(seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository);
    }

    @Bean("bookingServiceWithOptimistic")
    public BookingCommandService bookingServiceV5(
            @Qualifier("seatServiceWithOptimistic") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository
    ) {
        return new BookingCommandServiceImplV5(seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository);
    }

    @Bean("bookingServiceWithRedisLua")
    public BookingCommandService bookingServiceV6(
            MemberCommandService memberCommandService,
            @Qualifier("seatServiceWithJpa") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository,
            RedisTemplate<String, Object> redisTemplate
    ) {
        return new BookingCommandServiceImplV6(memberCommandService, seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository, redisTemplate);
    }

    @Bean("bookingServiceWithRedis")
    public BookingCommandService bookingServiceV7(
            MemberCommandService memberCommandService,
            @Qualifier("seatServiceWithPessimistic") SeatCommandService seatCommandService,
            MemberQueryService memberQueryService,
            SeatQueryService seatQueryService,
            ShowQueryService showQueryService,
            BookingCommandRepository bookingCommandRepository,
            RedisTemplate<String, Object> redisTemplate
    ) {
        return new BookingCommandServiceImplV6(memberCommandService, seatCommandService, memberQueryService, seatQueryService, showQueryService, bookingCommandRepository, redisTemplate);
    }
}
