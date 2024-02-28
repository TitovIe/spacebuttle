package ru.otus.spacebuttle.generator;

import lombok.extern.slf4j.Slf4j;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.UObject;
import ru.otus.spacebuttle.core.Ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Slf4j
public class ObjectGenerator {
    private static final Map<Integer, Object> idsToUObjects = new HashMap<>();
    private static final AtomicInteger uObjectId = new AtomicInteger();

    public static void init() {
        try {
            ((ICommand) Ioc.resolve("IoC.Register", "Игровые объекты", (Function<Object[], Object>) (Object[] args) -> idsToUObjects.get(args[0]))).execute();
        } catch (Exception e) {
            log.error("Objects initialization failed", e);
        }
    }

    public static <T extends UObject, V> void createObject(Class<T> clazz, Map<String, Object> properties, Class<V> adapterClazz) throws Exception {
        UObject uObject = clazz.getConstructor(Map.class).newInstance(properties);
        Object adapterObject = adapterClazz.getConstructor(UObject.class).newInstance(uObject);
        idsToUObjects.put(uObjectId.getAndIncrement(), adapterObject);
    }
}
