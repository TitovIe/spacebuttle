package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetValueCommand implements ICommand {
    private final UObject o;
    private final String key;
    private final Object value;

    @Override
    public void execute() throws Exception {
        o.setProperty(key, value);
    }
}
