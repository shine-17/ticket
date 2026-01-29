package study.ticket.infrastructure.query.show;

import study.ticket.domain.Show;

import java.util.Optional;

public interface ShowQueryRepository {
    Optional<Show> findById(long id);
}
