package ru.otus.spacebuttle;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class SpaceShip implements UObject {
    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String key, Object property) {
        properties.put(key, property);
    }
}
