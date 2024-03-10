package ru.otus.spacebuttle.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spacebuttle.Command;
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

    @Override
    public Command toCommand(SpaceButtleRequest spaceButtleRequest) {
        Command command = new Command();
        command.setProperty("OwnerId", spaceButtleRequest.getOwnerId());
        command.setProperty("GameId", spaceButtleRequest.getGameId());
        command.setProperty("ObjectId", spaceButtleRequest.getObjectId());
        command.setProperty("OperationId", spaceButtleRequest.getOperationId());
        command.setProperty("Args", spaceButtleRequest.getArgs());
        return command;
    }
}
