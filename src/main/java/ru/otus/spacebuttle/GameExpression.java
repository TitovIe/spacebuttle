package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.core.Ioc;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class GameExpression implements Expression {
    private final Expression actionExpression;

    @Override
    public Object interpret(UObject uObject) {
        BlockingQueue<ICommand> blockingQueue = Ioc.resolve("Очередь команд", uObject.getProperty("GameId"));
        blockingQueue.add((ICommand) actionExpression.interpret(uObject));
        return blockingQueue;
    }
}
