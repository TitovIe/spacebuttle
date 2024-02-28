package ru.otus.spacebuttle.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spacebuttle.InterpretCommand;
import ru.otus.spacebuttle.dto.SpaceButtleRequest;

@Component
public class SpaceButtleRequestMapperImpl implements SpaceButtleRequestMapper {
    @Override
    public InterpretCommand toInterpretCommand(SpaceButtleRequest spaceButtleRequest) {
        InterpretCommand interpretCommand = new InterpretCommand();
        interpretCommand.setGameId(spaceButtleRequest.getGameId());
        interpretCommand.setObjectId(spaceButtleRequest.getObjectId());
        interpretCommand.setOperationId(spaceButtleRequest.getOperationId());
        interpretCommand.setArgs(spaceButtleRequest.getArgs());
        return interpretCommand;
    }
}
