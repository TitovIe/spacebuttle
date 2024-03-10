package ru.otus.spacebuttle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spacebuttle.ExceptionHandler;
import ru.otus.spacebuttle.InterpretCommandNew;
import ru.otus.spacebuttle.core.Ioc;
import ru.otus.spacebuttle.dto.SpaceButtleRequest;
import ru.otus.spacebuttle.mapper.SpaceButtleRequestMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceButtleServiceImpl implements SpaceButtleService {
    private final SpaceButtleRequestMapper spaceButtleRequestMapper;

    @Override
    public Object makeAction(SpaceButtleRequest spaceButtleRequest) {
        InterpretCommandNew command = new InterpretCommandNew(spaceButtleRequestMapper.toCommand(spaceButtleRequest));
        try {
            command.execute();
            Thread.sleep(1000);
        } catch (Exception e) {
            try {
                ExceptionHandler.handle(e, command).execute();
            } catch (Exception ex) {
                log.error("Exception while handling exception", ex);
            }
        }

        return Ioc.resolve("Игровые объекты", spaceButtleRequest.getObjectId());
    }
}
