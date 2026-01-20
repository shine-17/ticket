package study.ticket.application.service;

import study.ticket.domain.Show;
import java.util.Optional;

public interface ShowService {
    Optional<Show> findById(long id);
}
