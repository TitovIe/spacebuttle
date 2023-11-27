package ru.otus.spacebuttle;

public class MoveException extends RuntimeException {
    public MoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
