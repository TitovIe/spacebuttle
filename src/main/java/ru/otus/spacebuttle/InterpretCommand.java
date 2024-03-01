package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.otus.spacebuttle.core.Ioc;
import ru.otus.spacebuttle.generator.GameGenerator;

import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

@Setter
@RequiredArgsConstructor
public class InterpretCommand implements ICommand {
    private Integer gameId;
    private Integer objectId;
    private Integer operationId;
    private Object[] args;

    @Override
    public void execute() throws Exception {
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", GameGenerator.getCurrentScope())).execute();

        Object uObject = Ioc.resolve("Игровые объекты", objectId);
        Object[] allArgs = new Object[]{uObject, args};
        Function<Object[], Object> function = Ioc.resolve("Операции", operationId);
        ICommand command = (ICommand) function.apply(allArgs);

        BlockingQueue<ICommand> blockingQueue = Ioc.resolve("Очередь команд", gameId);
        blockingQueue.add(command);
    }
}
