package ru.otus.spacebuttle;

import ru.otus.spacebuttle.core.Ioc;

import java.util.Objects;

public class ObjectExpression implements Expression {
    @Override
    public Object interpret(UObject uObject) {
        Object object = Ioc.resolve("Игровые объекты", uObject.getProperty("ObjectId"));
        UObject gameObject;
        try {
            gameObject = (UObject) object.getClass().getDeclaredField("uObject").get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!Objects.equals(gameObject.getProperty("OwnerId"), uObject.getProperty("OwnerId"))) {
            throw new RuntimeException();
        }

        return object;
    }
}
