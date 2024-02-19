package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.ServerThreadNew;

@RequiredArgsConstructor
public class RunCommand implements ICommand {
    private final ServerThreadNew serverThreadNew;
    private final BehaviourState simpleBehaviourState;

    @Override
    public void execute() throws Exception {
        serverThreadNew.setBehaviourState(simpleBehaviourState);
    }
}
