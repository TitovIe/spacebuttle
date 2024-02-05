package ru.otus.spacebuttle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class ExceptionHandler {
    private static Map<Class<? extends ICommand>, Map<Class<? extends Exception>, BiFunction<ICommand, Exception, ICommand>>> store = new HashMap<>();

    public static ICommand handle(Exception exception, ICommand command) {
        Class<? extends ICommand> commandClass = command.getClass();
        Class<? extends Exception> exceptionClass = exception.getClass();
        Map<Class<? extends Exception>, BiFunction<ICommand, Exception, ICommand>> classBiFunctionMap;
        BiFunction<ICommand, Exception, ICommand> function;

        classBiFunctionMap = store.get(commandClass);
        if (Objects.nonNull(classBiFunctionMap)) {
            function = classBiFunctionMap.get(exceptionClass);
        } else {
            return new ExceptionLogCommand(exception);
        }
        return Objects.nonNull(function) ? function.apply(command, exception) : new ExceptionLogCommand(exception);
    }

    public static void registerHandler(Class<? extends ICommand> commandClass,
                                       Class<? extends Exception> exceptionClass,
                                       BiFunction<ICommand, Exception, ICommand> function) {
        Map<Class<? extends Exception>, BiFunction<ICommand, Exception, ICommand>> classBiFunctionMap = store.get(commandClass);
        if (Objects.isNull(classBiFunctionMap)) {
            store.put(commandClass, new HashMap<>());
            store.get(commandClass).put(exceptionClass, function);
        }
        else {
            classBiFunctionMap.put(exceptionClass, function);
        }
    }
}
