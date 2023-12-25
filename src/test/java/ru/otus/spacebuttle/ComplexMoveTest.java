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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComplexMoveTest {
    @Mock
    IMovable iMovable;
    @Mock
    IBurnable iBurnable;
    @InjectMocks
    CheckFuelCommand checkFuelCommand;
    @InjectMocks
    MoveCommand moveCommand;
    @InjectMocks
    BurnFuelCommand burnFuelCommand;
    @Captor
    ArgumentCaptor<Double> resultFuelCaptor;
    @Captor
    ArgumentCaptor<Vector> resultPositionCaptor;

    @SneakyThrows
    @Test
    void execute_should_check_move_burn() {
        //Given
        MacroCommand macroCommand = new MacroCommand(new ICommand[] {checkFuelCommand, moveCommand, burnFuelCommand});
        Vector position = new Vector(12.0, 5.0);
        Vector velocity = new Vector(2.0, 2.0);
        Vector resultPosition = new Vector(14.0, 7.0);
        Double initFuel = 10.0;
        Double initVelocity = 4.0;
        Double resultFuel = 6.0;
        //When
        when(iBurnable.getFuel()).thenReturn(initFuel);
        when(iBurnable.getBurnVelocity()).thenReturn(initVelocity);
        when(iMovable.getPosition()).thenReturn(position);
        when(iMovable.getVelocity()).thenReturn(velocity);
        macroCommand.execute();
        //Then
        verify(iBurnable, times(2)).getFuel();
        verify(iBurnable, times(2)).getBurnVelocity();
        verify(iBurnable, times(1)).setFuel(resultFuelCaptor.capture());
        assertEquals(resultFuelCaptor.getValue(), resultFuel);
        verify(iMovable, times(1)).getVelocity();
        verify(iMovable, times(1)).getPosition();
        verify(iMovable, times(1)).setPosition(resultPositionCaptor.capture());
        assertEquals(resultPositionCaptor.getValue(), resultPosition);
    }
}
