package study.ticket.application.service.query.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.ticket.domain.Show;
import study.ticket.infrastructure.command.show.ShowCommandRepository;
import study.ticket.infrastructure.query.show.ShowQueryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowQueryServiceImpl implements ShowQueryService {

    private final ShowQueryRepository showQueryRepository;

    @Override
    public Optional<Show> findById(long id) {
        return showQueryRepository.findById(id);
    }
}
