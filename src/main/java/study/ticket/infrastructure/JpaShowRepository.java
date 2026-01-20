package study.ticket.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import study.ticket.domain.Show;

import java.util.Optional;

@Repository
public class JpaShowRepository implements ShowRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaPessimisticSeatRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Show> findById(long id) {
        return Optional.ofNullable(em.find(Show.class, id));
    }
}
