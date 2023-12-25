package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepeatCommand implements ICommand {
    private final ICommand command;

    @Override
    public void execute() throws Exception {
        command.execute();
    }
}
