package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class MovableAdapter implements IMovable {
    protected final UObject uObject;

    @Override
    public Vector getPosition() {
        return (Vector) uObject.getProperty("Position");
    }

    @Override
    public void setPosition(Vector newV) throws Exception {
        uObject.setProperty("Position", newV);
    }

    @Override
    public Vector getVelocity() {
        int d = (int) uObject.getProperty("Direction");
        int n = (int) uObject.getProperty("DirectionsNumber");
        double v = (double) uObject.getProperty("Velocity");
        return new Vector(
                v * Math.cos((double) (d / 360 * n)),
                v * Math.sin((double) (d / 360 * n)));
    }

    @Override
    public void finish() throws Exception {}
}
