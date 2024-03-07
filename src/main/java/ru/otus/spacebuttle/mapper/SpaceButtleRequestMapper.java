package ru.otus.spacebuttle.mapper;

import ru.otus.spacebuttle.Command;
import ru.otus.spacebuttle.InterpretCommand;
import ru.otus.spacebuttle.dto.SpaceButtleRequest;

public interface SpaceButtleRequestMapper {
    InterpretCommand toInterpretCommand(SpaceButtleRequest spaceButtleRequest);
    Command toCommand(SpaceButtleRequest spaceButtleRequest);
}
