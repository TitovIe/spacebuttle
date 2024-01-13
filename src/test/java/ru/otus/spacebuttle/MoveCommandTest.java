package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoveCommandTest {
    @Captor
    ArgumentCaptor<Vector> vectorArgumentCaptor;
    @Mock
    private IMovable movable;
    @InjectMocks
    private MoveCommand moveCommand;

    @SneakyThrows
    @Test
    void execute_should_set_right_vector_when_valid_position_and_velocity() {
        //Given
        Vector position = new Vector(12.0, 5.0);
        Vector velocity = new Vector(-7.0, 3.0);
        Vector result = new Vector(5.0, 8.0);

        //When
        when(movable.getPosition()).thenReturn(position);
        when(movable.getVelocity()).thenReturn(velocity);
        moveCommand.execute();

        //Then
        verify(movable, times(1)).setPosition(vectorArgumentCaptor.capture());
        assertEquals(vectorArgumentCaptor.getValue(), result);
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_not_valid_position() {
        //Given
        Vector position = new Vector(null, 5.0);
        Vector velocity = new Vector(-7.0, 3.0);

        //When
        when(movable.getPosition()).thenReturn(position);
        when(movable.getVelocity()).thenReturn(velocity);

        //Then
        assertThrows(MoveException.class, () -> moveCommand.execute());
        verify(movable, times(0)).setPosition(vectorArgumentCaptor.capture());
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_not_valid_velocity() {
        //Given
        Vector position = new Vector(12.0, 5.0);
        Vector velocity = new Vector(null, 3.0);

        //When
        when(movable.getPosition()).thenReturn(position);
        when(movable.getVelocity()).thenReturn(velocity);

        //Then
        assertThrows(MoveException.class, () -> moveCommand.execute());
        verify(movable, times(0)).setPosition(vectorArgumentCaptor.capture());
    }

    @SneakyThrows
    @Test
    void execute_should_throw_exception_when_not_valid_result() {
        //Given
        Vector position = new Vector(12.0, 5.0);
        Vector velocity = new Vector(-7.0, 3.0);

        //When
        when(movable.getPosition()).thenReturn(position);
        when(movable.getVelocity()).thenReturn(velocity);
        doThrow(RuntimeException.class).when(movable).setPosition(any());

        //Then
        assertThrows(MoveException.class, () -> moveCommand.execute());
        verify(movable, times(1)).setPosition(vectorArgumentCaptor.capture());
    }
}