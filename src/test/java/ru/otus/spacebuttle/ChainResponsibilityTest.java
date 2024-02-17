package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ChainResponsibilityTest {
    private List<Context> contexts;

    @SneakyThrows
    @BeforeEach
    void setup() {
        // Two areas with step 1.0 and shift 0.5, size of object is 0.3
        contexts = List.of(
                new Context("Fist",0.3, 1.0, 0.5,10.5, 0.5, 10.5),
                new Context("Second",0.3, 1.0, 0.0, 10.0, 0.0, 10.0));
    }

    @Test
    void execute_should_throw_IllegalStateException_because_collision_when_two_contexts() {
        BlockingDeque<ICommand> blockingDeque = new LinkedBlockingDeque<>();

        UObject uObject1 = new SpaceShip();
        uObject1.setProperty("Position", new Vector(0.9, 0.9));
        contexts.forEach(context -> uObject1.setProperty("Cell_" + context.getTitle(), null));
        DetermineCellCommand determineCellCommand1 = new DetermineCellCommand(contexts, uObject1, blockingDeque);

        UObject uObject2 = new SpaceShip();
        uObject2.setProperty("Position", new Vector(1.5, 1.5));
        contexts.forEach(context -> uObject2.setProperty("Cell_" + context.getTitle(), null));
        DetermineCellCommand determineCellCommand2 = new DetermineCellCommand(contexts, uObject2, blockingDeque);

        UObject uObject3 = new SpaceShip();
        uObject3.setProperty("Position", new Vector(1.1, 1.1));
        contexts.forEach(context -> uObject3.setProperty("Cell_" + context.getTitle(), null));
        DetermineCellCommand determineCellCommand3 = new DetermineCellCommand(contexts, uObject3, blockingDeque);

        UObject uObject4 = new SpaceShip();
        uObject4.setProperty("Position", new Vector(5.0, 4.0));
        contexts.forEach(context -> uObject4.setProperty("Cell_" + context.getTitle(), null));
        DetermineCellCommand determineCellCommand4 = new DetermineCellCommand(contexts, uObject4, blockingDeque);

        UObject uObject5 = new SpaceShip();
        uObject5.setProperty("Position", new Vector(7.0, 9.0));
        contexts.forEach(context -> uObject5.setProperty("Cell_" + context.getTitle(), null));
        DetermineCellCommand determineCellCommand5 = new DetermineCellCommand(contexts, uObject5, blockingDeque);

        blockingDeque.addAll(List.of(determineCellCommand1, determineCellCommand2, determineCellCommand3, determineCellCommand4, determineCellCommand5,
                determineCellCommand1, determineCellCommand2, determineCellCommand3, determineCellCommand4, determineCellCommand5));

        /*
        Collision between
        SpaceShip(properties={Cell_Second=Cell[x1=1.0, x2=2.0, y1=1.0, y2=2.0], Position=Vector[x=1.1, y=1.1], Cell_Fist=Cell[x1=0.5, x2=1.5, y1=0.5, y2=1.5]})
        and SpaceShip(properties={Cell_Second=Cell[x1=0.0, x2=1.0, y1=0.0, y2=1.0], Position=Vector[x=0.9, y=0.9], Cell_Fist=Cell[x1=0.5, x2=1.5, y1=0.5, y2=1.5]})
        */
        System.out.println(assertThrows(IllegalStateException.class, () -> {
            while (true) {
                blockingDeque.take().execute();
            }
        }).getMessage());
    }
}
