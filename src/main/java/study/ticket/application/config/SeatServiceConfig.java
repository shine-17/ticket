package study.ticket.application.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ticket.application.service.command.seat.SeatCommandService;
import study.ticket.application.service.command.seat.SeatCommandServiceImpl;
import study.ticket.infrastructure.command.seat.SeatCommandRepository;

@Configuration
public class SeatServiceConfig {

    @Bean("seatServiceWithJpa")
    public SeatCommandService seatServiceWithJpa(@Qualifier("jpaSeatRepository") SeatCommandRepository repository) {
        return new SeatCommandServiceImpl(repository);
    }

    @Bean("seatServiceWithPessimistic")
    public SeatCommandService seatServiceWithPessimistic(@Qualifier("jpaPessimisticSeatRepository") SeatCommandRepository repository) {
        return new SeatCommandServiceImpl(repository);
    }

    @Bean("seatServiceWithOptimistic")
    public SeatCommandService seatServiceWithOptimistic(@Qualifier("jpaOptimisticSeatRepository") SeatCommandRepository repository) {
        return new SeatCommandServiceImpl(repository);
    }
}
