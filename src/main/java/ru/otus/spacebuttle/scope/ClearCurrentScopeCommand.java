package ru.otus.spacebuttle.scope;

import ru.otus.spacebuttle.ICommand;

public class ClearCurrentScopeCommand implements ICommand {
    @Override
    public void execute() throws Exception {
        InitCommand.currentScope.remove();
    }
}
