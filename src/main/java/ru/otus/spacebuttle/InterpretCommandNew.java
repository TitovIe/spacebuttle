package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.core.Ioc;
import ru.otus.spacebuttle.generator.GameGenerator;

@RequiredArgsConstructor
public class InterpretCommandNew implements ICommand {
    private final UObject uObject;

    @Override
    public void execute() throws Exception {
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", GameGenerator.getCurrentScope())).execute();
        Expression gameExpression = buildExpressionTree();
        gameExpression.interpret(uObject);
    }

    private Expression buildExpressionTree() {
        return new GameExpression(
                new ActionExpression(
                        new ObjectExpression(),
                        new ArgumentsExpression()
                )
        );
    }
}
