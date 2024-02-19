package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.ServerThreadNew;

@RequiredArgsConstructor
public class HardStopNewCommand implements ICommand {
    private final ServerThreadNew serverThreadNew;

    @Override
    public void execute() throws Exception {
        serverThreadNew.setBehaviourState(null);
    }
}
