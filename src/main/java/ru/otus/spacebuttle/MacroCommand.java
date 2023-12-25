package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MacroCommand implements ICommand {
    protected final ICommand[] iCommands;

    @Override
    public void execute() throws Exception {
        for (ICommand iCommand : iCommands) {
            iCommand.execute();
        }
    }
}
