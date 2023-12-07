package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MacroCommand implements ICommand {
    protected final ICommand[] iCommands;

    @Override
    public void Execute() throws Exception {
        for (ICommand iCommand : iCommands) {
            iCommand.Execute();
        }
    }
}
