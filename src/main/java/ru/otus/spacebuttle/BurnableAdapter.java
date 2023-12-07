package ru.otus.spacebuttle;

public class BurnableAdapter extends MovableAdapter implements IBurnable {
    public BurnableAdapter(UObject o) {
        super(o);
    }

    @Override
    public Double getBurnVelocity() {
        return (Double) o.getProperty("Velocity");
    }

    @Override
    public Double getFuel() {
        return (Double) o.getProperty("Fuel");
    }

    @Override
    public void setFuel(Double fuel) {
        o.setProperty("Fuel", fuel);
    }
}
