package ru.otus.spacebuttle.generator;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.Ioc;
import ru.otus.spacebuttle.core.ServerThread;
import ru.otus.spacebuttle.scope.InitCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Slf4j
public class GameGenerator {
    private static final Integer CAPACITY = 10;
    private static final Map<Integer, BlockingQueue<ICommand>> idsToGames = new HashMap<>();
    private static final AtomicInteger gameId = new AtomicInteger();
    @Getter
    @Setter
    private static Object currentScope;

    public static void init() {
        try {
            new InitCommand().execute();
            var iocScope = Ioc.resolve("IoC.Scope.Create");
            ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();
            ((ICommand) Ioc.resolve("IoC.Register", "Очередь команд", (Function<Object[], Object>) (Object[] args) -> idsToGames.get(args[0]))).execute();

            currentScope = Ioc.resolve("IoC.Scope.Current");
        } catch (Exception e) {
            log.error("Game initialization failed", e);
        }
    }

    public static void createGame() {
        BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(CAPACITY);
        new ServerThread(blockingQueue).start();
        idsToGames.put(gameId.getAndIncrement(), blockingQueue);
    }
}
