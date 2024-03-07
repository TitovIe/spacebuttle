package ru.otus.spacebuttle;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class Command implements UObject {
    private final Map<String, Object> properties ;

    public Command() {
        properties = new HashMap<>();
    }

    public Command(Map<String, Object> properties) {
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
