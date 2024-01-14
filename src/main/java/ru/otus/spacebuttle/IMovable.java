package ru.otus.spacebuttle;

public interface IMovable {
    Vector getPosition();

    void setPosition(Vector position) throws Exception;

    Vector getVelocity();

    void finish() throws Exception;
}
