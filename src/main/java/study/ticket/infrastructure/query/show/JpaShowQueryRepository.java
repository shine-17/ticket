package study.ticket.infrastructure.query.show;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Show;
import study.ticket.infrastructure.command.seat.JpaPessimisticSeatCommandRepository;

import java.util.Optional;

@Repository
public class JpaShowQueryRepository implements ShowQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaPessimisticSeatCommandRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Show> findById(long id) {
        return Optional.ofNullable(em.find(Show.class, id));
    }
}
