package ru.otus.spacebuttle.core.Impl;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.Ioc;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public class UpdateIocResolveDependencyStrategyCommand implements ICommand {
    private final Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> updateIoCStrategy;

    @Override
    public void execute() throws Exception {
        Ioc.strategy = updateIoCStrategy.apply(Ioc.strategy);
    }
}
