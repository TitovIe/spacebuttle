package ru.otus.spacebuttle;

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
class RotateTest {
    @Captor
    ArgumentCaptor<Integer> newDirection;
    @Mock
    private IRotable rotable;
    @InjectMocks
    private Rotate rotate;

    @Test
    void execute_should_set_right_direction_when_valid_direction_and_angularVelocity_and_directionsNumber() {
        //Given
        Integer direction = 5;
        Integer directionsNumber = 6;
        Double angularVelocity = 1.2;
        Integer result = 0;

        //When
        when(rotable.getDirection()).thenReturn(direction);
        when(rotable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotable.getDirectionsNumber()).thenReturn(directionsNumber);
        rotate.Execute();

        //Then
        verify(rotable, times(1)).setDirection(newDirection.capture());
        assertEquals(newDirection.getValue(), result);
    }

    @Test
    void execute_should_throw_exception_when_not_valid_direction() {
        //Given
        Integer direction = null;
        Integer directionsNumber = 6;
        Double angularVelocity = 1.2;

        //When
        when(rotable.getDirection()).thenReturn(direction);
        when(rotable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotable.getDirectionsNumber()).thenReturn(directionsNumber);

        //Then
        assertThrows(RotateException.class, () -> rotate.Execute());
        verify(rotable, times(0)).setDirection(newDirection.capture());
    }

    @Test
    void execute_should_throw_exception_when_not_valid_angularVelocity() {
        //Given
        Integer direction = 5;
        Integer directionsNumber = null;
        Double angularVelocity = 1.2;

        //When
        when(rotable.getDirection()).thenReturn(direction);
        when(rotable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotable.getDirectionsNumber()).thenReturn(directionsNumber);

        //Then
        assertThrows(RotateException.class, () -> rotate.Execute());
        verify(rotable, times(0)).setDirection(newDirection.capture());
    }

    @Test
    void execute_should_throw_exception_when_not_valid_directionsNumber() {
        //Given
        Integer direction = 5;
        Integer directionsNumber = 6;
        Double angularVelocity = null;

        //When
        when(rotable.getDirection()).thenReturn(direction);
        when(rotable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotable.getDirectionsNumber()).thenReturn(directionsNumber);

        //Then
        assertThrows(RotateException.class, () -> rotate.Execute());
        verify(rotable, times(0)).setDirection(newDirection.capture());
    }

    @Test
    void execute_should_throw_exception_when_not_valid_result() {
        //Given
        Integer direction = 5;
        Integer directionsNumber = 6;
        Double angularVelocity = 1.2;

        //When
        when(rotable.getDirection()).thenReturn(direction);
        when(rotable.getAngularVelocity()).thenReturn(angularVelocity);
        when(rotable.getDirectionsNumber()).thenReturn(directionsNumber);
        doThrow(RuntimeException.class).when(rotable).setDirection(any());

        //Then
        assertThrows(RotateException.class, () -> rotate.Execute());
        verify(rotable, times(1)).setDirection(newDirection.capture());
    }
}