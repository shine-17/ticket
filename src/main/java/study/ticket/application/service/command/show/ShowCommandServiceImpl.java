package study.ticket.application.service.command.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.ticket.infrastructure.command.show.ShowCommandRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowCommandServiceImpl implements ShowCommandService {

    private final ShowCommandRepository showCommandRepository;

}
