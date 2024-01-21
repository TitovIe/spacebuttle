package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.ServerThread;

@RequiredArgsConstructor
public class HardStopCommand implements ICommand {
    private final ServerThread serverThread;

    @Override
    public void execute() {
        serverThread.stop();
    }
}
