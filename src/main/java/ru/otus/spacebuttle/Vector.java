package ru.otus.spacebuttle;

public record Vector(Double x, Double y) {
    static Vector plus(Vector vector1, Vector vector2) {
        return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
    }
}
