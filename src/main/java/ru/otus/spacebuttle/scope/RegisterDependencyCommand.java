package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.Ioc;

import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
public class RegisterDependencyCommand implements ICommand {
    private final String dependency;
    private final Function<Object[], Object> dependencyResolverStrategy;

    @Override
    public void execute() throws Exception {
        Map<String, Function<Object[], Object>> currentScope = Ioc.resolve("IoC.Scope.Current");
        currentScope.put(dependency, dependencyResolverStrategy);
    }
}
