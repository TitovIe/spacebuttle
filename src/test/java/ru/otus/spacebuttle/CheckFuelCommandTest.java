package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckFuelCommandTest {
    @Mock
    IBurnable iBurnable;
    @InjectMocks
    CheckFuelCommand checkFuelCommand;

    @SneakyThrows
    @Test
    void execute_should_check_fuel_and_do_nothing() {
        //Given
        Double fuel = 6.0;
        Double velocity = 5.0;

        //When
        when(iBurnable.getFuel()).thenReturn(fuel);
        when(iBurnable.getBurnVelocity()).thenReturn(velocity);
        checkFuelCommand.Execute();

        //Then
        verify(iBurnable, times(1)).getFuel();
        verify(iBurnable, times(1)).getBurnVelocity();
    }

    @SneakyThrows
    @Test
    void execute_should_check_fuel_and_throw_exception() {
        //Given
        Double fuel = 5.0;
        Double velocity = 6.0;

        //When
        when(iBurnable.getFuel()).thenReturn(fuel);
        when(iBurnable.getBurnVelocity()).thenReturn(velocity);

        //Then
        assertThrows(CheckFuelException.class, () -> checkFuelCommand.Execute());
        verify(iBurnable, times(1)).getFuel();
        verify(iBurnable, times(1)).getBurnVelocity();
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_getFuel_throw_exception() {
        //Given
        //When
        when(iBurnable.getFuel()).thenThrow(new RuntimeException());

        //Then
        assertThrows(CheckFuelException.class, () -> checkFuelCommand.Execute());
        verify(iBurnable, times(1)).getFuel();
        verify(iBurnable, times(0)).getBurnVelocity();
    }
}
