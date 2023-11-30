package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveCommand implements ICommand {
    private final IMovable movable;

    @Override
    public void Execute() {
        try {
            movable.setPosition(Vector.plus(
                    movable.getPosition(),
                    movable.getVelocity()
            ));
        } catch (Exception e) {
            throw new MoveException(String.format("Exception while execute %s", movable), e);
        }
    }
}
