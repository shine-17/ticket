package study.ticket.application.service.query.show;

import study.ticket.domain.Show;

import java.util.Optional;

public interface ShowQueryService {
    Optional<Show> findById(long id);
}
