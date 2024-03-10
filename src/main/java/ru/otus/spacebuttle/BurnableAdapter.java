package ru.otus.spacebuttle;

public class BurnableAdapter extends MovableAdapter implements IBurnable {
    public BurnableAdapter(UObject o) {
        super(o);
    }

    @Override
    public Double getBurnVelocity() {
        return (Double) uObject.getProperty("Velocity");
    }

    @Override
    public Double getFuel() {
        return (Double) uObject.getProperty("Fuel");
    }

    @Override
    public void setFuel(Double fuel) {
        uObject.setProperty("Fuel", fuel);
    }
}
