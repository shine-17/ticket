package study.ticket.infrastructure.command.show;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import study.ticket.infrastructure.command.seat.JpaPessimisticSeatCommandRepository;

@Repository
public class JpaShowCommandRepository implements ShowCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaPessimisticSeatCommandRepository.class);

    @PersistenceContext
    private EntityManager em;

}
