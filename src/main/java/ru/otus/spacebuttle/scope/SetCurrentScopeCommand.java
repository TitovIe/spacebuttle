package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;

@RequiredArgsConstructor
public class SetCurrentScopeCommand implements ICommand {
    private final Object scope;

    @Override
    public void execute() throws Exception {
        InitCommand.currentScope.set(scope);
    }
}
