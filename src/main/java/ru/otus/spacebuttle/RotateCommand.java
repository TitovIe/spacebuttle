package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RotateCommand implements ICommand {
    private final IRotable rotable;

    @Override
    public void execute() {
        try {
            rotable.setDirection((int) ((rotable.getDirection() + rotable.getAngularVelocity()) % rotable.getDirectionsNumber()));
        } catch (Exception e) {
            throw new RotateException(String.format("Exception while execute %s", rotable), e);
        }
    }
}
