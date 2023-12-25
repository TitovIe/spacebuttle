package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BurnFuelCommand implements ICommand {
    private final IBurnable iBurnable;

    @Override
    public void execute() throws Exception {
        Double fuel;
        Double velocity;
        try {
            fuel = iBurnable.getFuel();
            velocity = iBurnable.getBurnVelocity();
            iBurnable.setFuel(fuel - velocity);
        } catch (Exception e) {
            throw new BurnFuelException("Ошибка во время сжигания топлива");
        }
    }
}
