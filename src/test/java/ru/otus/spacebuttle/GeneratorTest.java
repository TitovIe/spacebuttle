package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spacebuttle.core.Ioc;
import ru.otus.spacebuttle.generator.SourceCodeGenerator;
import ru.otus.spacebuttle.scope.InitCommand;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneratorTest {
    @Mock
    private UObject uObject;

    @SneakyThrows
    @BeforeAll
    static void setup() {
        new InitCommand().execute();
        var iocScope = Ioc.resolve("IoC.Scope.Create");
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();

        SourceCodeGenerator sourceCodeGenerator = new SourceCodeGenerator();
        sourceCodeGenerator.generateAdapterClassFromInterface(IMovable.class);
    }

    @SneakyThrows
    @Test
    void should_generate_adapter_and_make_get_set_logic() {
        //Given
        ((ICommand) Ioc.resolve("IoC.Register", "getPosition", (Function<Object[], Object>) (Object[] args) ->
                (Vector) ((UObject) args[0]).getProperty("Position"))).execute();
        ((ICommand) Ioc.resolve("IoC.Register", "setPosition", (Function<Object[], Object>) (Object[] args) ->
                new SetValueCommand((UObject) args[0], "Position", args[1]))).execute();
        ((ICommand) Ioc.resolve("IoC.Register", "getVelocity", (Function<Object[], Object>) (Object[] args) ->
        {
            UObject o = (UObject) args[0];
            int d = (int) o.getProperty("Direction");
            int n = (int) o.getProperty("DirectionsNumber");
            double v = (double) o.getProperty("Velocity");
            return new Vector(
                    v * Math.cos((double) (d / 360 * n)),
                    v * Math.sin((double) (d / 360 * n)));
        })).execute();

        Vector position = new Vector(12.0, 5.0);
        Double velocityMod = 5.0;
        Vector velocity = new Vector(5.0, 0.0);
        Integer direction = 0;
        Integer directionsNumber = 6;

        IMovable instanceOfClass = Ioc.resolve("MovableAdapter", UObject.class, uObject);
        //When
        when(uObject.getProperty("Position")).thenReturn(position);
        when(uObject.getProperty("Velocity")).thenReturn(velocityMod);
        when(uObject.getProperty("Direction")).thenReturn(direction);
        when(uObject.getProperty("DirectionsNumber")).thenReturn(directionsNumber);
        doNothing().when(uObject).setProperty("Position", position);

        Vector resultPosition = instanceOfClass.getPosition();
        Vector resultVelocity = instanceOfClass.getVelocity();
        instanceOfClass.setPosition(position);

        //Then
        assertEquals(position, resultPosition);
        assertEquals(velocity, resultVelocity);
        verify(uObject, times(1)).getProperty("Position");
        verify(uObject, times(1)).getProperty("Velocity");
        verify(uObject, times(1)).getProperty("Direction");
        verify(uObject, times(1)).getProperty("DirectionsNumber");
        verify(uObject, times(1)).setProperty("Position", position);
    }

    @SneakyThrows
    @Test
    void should_generate_adapter_and_make_finish_logic() {
        //Given
        ((ICommand) Ioc.resolve("IoC.Register", "finish", (Function<Object[], Object>) (Object[] args) ->
                new FinishCommand(uObject))).execute();

        Vector position = new Vector(12.0, 5.0);
        Double velocityMod = 5.0;

        //When
        when(uObject.getProperty("Position")).thenReturn(position);
        when(uObject.getProperty("Velocity")).thenReturn(velocityMod);

        IMovable instanceOfClass = Ioc.resolve("MovableAdapter", UObject.class, uObject);
        instanceOfClass.finish();

        //Then
        verify(uObject, times(1)).getProperty("Position");
        verify(uObject, times(1)).getProperty("Velocity");
    }
}
