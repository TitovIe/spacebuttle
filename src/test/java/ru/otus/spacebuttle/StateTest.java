package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spacebuttle.core.ServerThreadNew;
import ru.otus.spacebuttle.scope.*;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StateTest {
    private Consumer<BlockingQueue<ICommand>> behaviour;
    private BlockingQueue<ICommand> blockingQueue;
    private BlockingQueue<ICommand> blockingQueueTo;

    @SneakyThrows
    @BeforeEach
    void setup() {
        behaviour = (blockingQueue) -> {
            ICommand command = null;
            try {
                command = blockingQueue.take();
            } catch (InterruptedException e) {
                log.error("Exception while thread waiting", e);
            }
            try {
                if (command != null) {
                    command.execute();
                }
            } catch (Exception e) {
                try {
                    ExceptionHandler.handle(e, command).execute();
                } catch (Exception ex) {
                    log.error("Exception while handling exception", ex);
                }
            }
        };

        blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueueTo = new ArrayBlockingQueue<>(10);
    }

    @SneakyThrows
    @Test
    void execute_should_stop_thread_when_hard_stop_command_called() {
        //Given
        int resultSize = 2;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        BehaviourState simpleBehaviourState = new SimpleBehaviourState(blockingQueue, behaviour);
        ServerThreadNew serverThreadNew = new ServerThreadNew(simpleBehaviourState);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        new MacroCommand(new ICommand[]{new HardStopNewCommand(serverThreadNew), new CountDownCommand(countDownLatch)}),
                        (ICommand) () -> System.out.println("command3"),
                        (ICommand) () -> System.out.println("command4")
                )
        );

        //When
        serverThreadNew.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, blockingQueue.size());
    }

    @SneakyThrows
    @Test
    void execute_should_replace_state_to_move_to_state_when_move_to_command_called() {
        //Given
        int resultSize = 0;
        int resultSizeTo = 1;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        BehaviourState simpleBehaviourState = new SimpleBehaviourState(blockingQueue, behaviour);
        BehaviourState moveToBehaviourState = new MoveToBehaviourState(blockingQueue, blockingQueueTo, behaviour);
        ServerThreadNew serverThreadNew = new ServerThreadNew(simpleBehaviourState);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        new MoveToCommand(serverThreadNew, moveToBehaviourState),
                        (ICommand) () -> System.out.println("command3"),
                        new MacroCommand(new ICommand[]{new HardStopNewCommand(serverThreadNew), new CountDownCommand(countDownLatch)}),
                        (ICommand) () -> System.out.println("command4")
                )
        );

        //When
        serverThreadNew.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, blockingQueue.size());
        assertEquals(resultSizeTo, blockingQueueTo.size());
    }

    @SneakyThrows
    @Test
    void execute_should_replace_state_to_simple_state_when_run_command_called() {
        //Given
        int resultSize = 0;
        int resultSizeTo = 1;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        BehaviourState simpleBehaviourState = new SimpleBehaviourState(blockingQueue, behaviour);
        BehaviourState moveToBehaviourState = new MoveToBehaviourState(blockingQueue, blockingQueueTo, behaviour);
        ServerThreadNew serverThreadNew = new ServerThreadNew(simpleBehaviourState);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        new MoveToCommand(serverThreadNew, moveToBehaviourState),
                        (ICommand) () -> System.out.println("command3"),
                        new MacroCommand(new ICommand[]{
                                new RunCommand(serverThreadNew, simpleBehaviourState),
                                new HardStopNewCommand(serverThreadNew),
                                new CountDownCommand(countDownLatch)}),
                        (ICommand) () -> System.out.println("command4")
                )
        );

        //When
        serverThreadNew.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, blockingQueue.size());
        assertEquals(resultSizeTo, blockingQueueTo.size());
    }
}
