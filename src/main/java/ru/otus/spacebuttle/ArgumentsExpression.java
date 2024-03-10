package ru.otus.spacebuttle;

public class ArgumentsExpression implements Expression {
    @Override
    public Object interpret(UObject uObject) {
        return uObject.getProperty("Args");
    }
}
