package ru.otus.spacebuttle;

public class Move {
    private final IMovable movable;

    public Move(IMovable movable) {
        this.movable = movable;
    }

    public void Execute() {
        try {
            movable.setPosition(Vector.plus(
                    movable.getPosition(),
                    movable.getVelocity()
            ));
        } catch (Exception e) {
            throw new MoveException(String.format("Exception while execute with position: %s and velocity: %s",
                    movable.getPosition(), movable.getVelocity()), e);
        }
    }
}
