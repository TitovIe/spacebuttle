package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovableAdapter implements IMovable {
    protected final UObject o;

    @Override
    public Vector getPosition() {
        return (Vector) o.getProperty("Position");
    }

    @Override
    public void setPosition(Vector newV) throws Exception {
        o.setProperty("Position", newV);
    }

    @Override
    public Vector getVelocity() {
        int d = (int) o.getProperty("Direction");
        int n = (int) o.getProperty("DirectionsNumber");
        double v = (double) o.getProperty("Velocity");
        return new Vector(
                v * Math.cos((double) (d / 360 * n)),
                v * Math.sin((double) (d / 360 * n)));
    }

    @Override
    public void finish() throws Exception {}
}
