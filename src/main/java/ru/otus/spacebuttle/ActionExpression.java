package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.core.Ioc;

import java.util.function.Function;

@RequiredArgsConstructor
public class ActionExpression implements Expression {
    private final Expression objectExpression;
    private final Expression argumentsExpression;

    @Override
    public Object interpret(UObject uObject) {
        Object[] allArgs = new Object[]{objectExpression.interpret(uObject), argumentsExpression.interpret(uObject)};
        Function<Object[], Object> function = Ioc.resolve("Операции", uObject.getProperty("OperationId"));
        return function.apply(allArgs);
    }
}
