package ru.otus.spacebuttle.generator;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spacebuttle.*;
import ru.otus.spacebuttle.core.Ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Slf4j
public class OperationGenerator {
    private static final Map<Integer, Function<Object[], Object>> idsToOperations = new HashMap<>();
    private static final AtomicInteger operationId = new AtomicInteger();

    public static void init() {
        try {
            ((ICommand) Ioc.resolve("IoC.Register", "Операции", (Function<Object[], Object>) (Object[] args) -> idsToOperations.get(args[0]))).execute();

            ((ICommand) Ioc.resolve(
                    "IoC.Register",
                    "Установить начальное значение скорости",
                    (Function<Object[], Object>) (Object[] args) -> new SetValueCommand((UObject) args[0], "Velocity", args[1]))).execute();
            ((ICommand) Ioc.resolve(
                    "IoC.Register",
                    "Установить начальное положение",
                    (Function<Object[], Object>) (Object[] args) -> new SetValueCommand((UObject) args[0], "Position", args[1]))).execute();
            ((ICommand) Ioc.resolve(
                    "IoC.Register",
                    "Движение по прямой",
                    (Function<Object[], Object>) (Object[] args) -> new MoveCommand((IMovable) args[0]))).execute();

            idsToOperations.put(operationId.getAndIncrement(), (Object[] args) -> Ioc.resolve("Установить начальное значение скорости", args));
            idsToOperations.put(operationId.getAndIncrement(), (Object[] args) -> Ioc.resolve("Установить начальное положение", args));
            idsToOperations.put(operationId.getAndIncrement(), (Object[] args) -> Ioc.resolve("Движение по прямой", args));
        } catch (Exception e) {
            log.error("Operation initialization failed");
        }
    }

    public static void createOperation(Integer id, Object[] args) throws Exception {
        Function<Object[], Object> function = Ioc.resolve("Операции", id);
        ((ICommand) function.apply(args)).execute();
    }
}
