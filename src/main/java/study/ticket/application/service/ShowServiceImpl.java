package study.ticket.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.ticket.domain.Show;
import study.ticket.infrastructure.ShowRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    @Override
    public Optional<Show> findById(long id) {
        return showRepository.findById(id);
    }
}
