package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepeatDoubleCommand implements ICommand {
    private final ICommand command;

    @Override
    public void Execute() throws Exception {
        command.Execute();
    }
}
