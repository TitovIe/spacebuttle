package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BurnFuelCommandTest {
    @Captor
    ArgumentCaptor<Double> fuelResult;
    @Mock
    IBurnable iBurnable;
    @InjectMocks
    BurnFuelCommand burnFuelCommand;

    @SneakyThrows
    @Test
    void execute_should_burn_fuel() {
        //Given
        Double fuel = 6.0;
        Double velocity = 5.0;

        //When
        when(iBurnable.getFuel()).thenReturn(fuel);
        when(iBurnable.getBurnVelocity()).thenReturn(velocity);
        burnFuelCommand.Execute();

        //Then
        verify(iBurnable, times(1)).setFuel(fuelResult.capture());
        verify(iBurnable, times(1)).getFuel();
        verify(iBurnable, times(1)).getBurnVelocity();
        assertEquals(fuelResult.getValue(), 1.0);
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_getFuel_throw_exception() {
        //Given
        //When
        doThrow(new RuntimeException()).when(iBurnable).getFuel();

        //Then
        assertThrows(BurnFuelException.class, () -> burnFuelCommand.Execute());
        verify(iBurnable, times(1)).getFuel();
        verify(iBurnable, times(0)).getBurnVelocity();
    }
}
