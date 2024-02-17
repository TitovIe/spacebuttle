package ru.otus.spacebuttle;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {
    @Getter
    private final String title;
    @Getter
    private final Double condition;
    private final Double step;
    private final Double minX;
    private final Double maxX;
    private final Double minY;
    private final Double maxY;
    @Getter
    private final Map<Cell, Set<UObject>> locationContext;

    public Context(String title, Double condition, Double step, Double minX, Double maxX, Double minY, Double maxY) {
        this.title = title;
        this.condition = condition;
        this.step = step;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.locationContext = new HashMap<>();
        initLocationContext();
    }

    private void initLocationContext() {
        int cellNumberX = (int) ((maxX - minX) / step);
        int cellNumberY = (int) ((maxY - minY) / step);
        for (int i = 0; i < cellNumberX; i++) {
            for (int j = 0; j < cellNumberY; j++) {
                locationContext.put(new Cell(minX + i * step, minX + (i + 1) * step, minY + j * step, minY + (j + 1) * step), new HashSet<>());
            }
        }
    }
}
