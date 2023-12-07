package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MacroCommandTest {
    private ICommand checkFuelCommand;
    private ICommand moveCommand;
    private ICommand burnFuelCommand;
    private MacroCommand macroCommand;

    @BeforeEach
    void init() {
        checkFuelCommand = mock(CheckFuelCommand.class);
        moveCommand = mock(MoveCommand.class);
        burnFuelCommand = mock(BurnFuelCommand.class);
        macroCommand = new MacroCommand(new ICommand[]{checkFuelCommand, moveCommand, burnFuelCommand});
    }

    @SneakyThrows
    @Test
    void execute_should_check_move_burn() {
        //Given
        //When
        doNothing().when(checkFuelCommand).Execute();
        doNothing().when(moveCommand).Execute();
        doNothing().when(burnFuelCommand).Execute();
        macroCommand.Execute();

        //Then
        InOrder inOrder = inOrder(checkFuelCommand, moveCommand, burnFuelCommand);

        inOrder.verify(checkFuelCommand, times(1)).Execute();
        inOrder.verify(moveCommand, times(1)).Execute();
        inOrder.verify(burnFuelCommand, times(1)).Execute();
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_thrown_exception() {
        //Given
        //When
        doThrow(CheckFuelException.class).when(checkFuelCommand).Execute();
        //Then
        assertThrows(CheckFuelException.class, () -> macroCommand.Execute());
        verify(moveCommand, times(0)).Execute();
        verify(burnFuelCommand, times(0)).Execute();
    }
}
