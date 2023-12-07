package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CheckFuelCommand implements ICommand {
    private final IBurnable iBurnable;

    @Override
    public void Execute() throws Exception {
        Double fuel;
        Double velocity;
        try {
            fuel = iBurnable.getFuel();
            velocity = iBurnable.getBurnVelocity();
        } catch (Exception e) {
            throw new CheckFuelException("Ошибка во время проверки наличия топлива");
        }
        if (velocity > fuel)
            throw new CheckFuelException(String.format("Недостаточно топлива для движения: %s < %s", fuel, velocity));
    }
}
