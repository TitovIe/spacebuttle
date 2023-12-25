package ru.otus.spacebuttle.core;

import ru.otus.spacebuttle.core.Impl.UpdateIocResolveDependencyStrategyCommand;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Ioc {
    public static BiFunction<String, Object[], Object> strategy = (String dependency, Object[] args) ->
    {
        if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
            return new UpdateIocResolveDependencyStrategyCommand((Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>) args[0]);
        } else {
            throw new IllegalArgumentException(String.format("Dependency %s is not found.", dependency));
        }
    };

    public static <T> T resolve(String dependency, Object... args)
    {
        return (T) strategy.apply(dependency, args);
    }
}
