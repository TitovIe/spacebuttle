package ru.otus.spacebuttle;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class SpaceShip implements UObject {
    private final Map<String, Object> properties ;

    public SpaceShip() {
        properties = new HashMap<>();
    }

    public SpaceShip(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String key, Object property) {
        properties.put(key, property);
    }
}
