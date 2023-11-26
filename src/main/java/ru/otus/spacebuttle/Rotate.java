package ru.otus.spacebuttle;

public class Rotate {
    private final IRotable rotable;

    public Rotate(IRotable rotable) {
        this.rotable = rotable;
    }

    public void Execute() {
        try {
            rotable.setDirection((int) ((rotable.getDirection() + rotable.getAngularVelocity()) % rotable.getDirectionsNumber()));
        } catch (Exception e) {
            throw new RotateException(String.format("Exception while execute with direction: %s, angularVelocity: %s, directionsNumber: %s",
                    rotable.getDirection(), rotable.getAngularVelocity(), rotable.getDirectionsNumber()), e);
        }
    }
}
