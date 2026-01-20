package study.ticket.infrastructure;

import study.ticket.domain.Show;

import java.util.Optional;

public interface ShowRepository {
    Optional<Show> findById(long id);
}
