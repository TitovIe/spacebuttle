package ru.otus.spacebuttle;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spacebuttle.core.ServerThread;
import ru.otus.spacebuttle.scope.CountDownCommand;
import ru.otus.spacebuttle.scope.HardStopCommand;
import ru.otus.spacebuttle.scope.SoftStopCommand;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ConcurrentTest {

    @SneakyThrows
    @Test
    void should_execute_all_commands_despite_exception() {
        //Given
        int resultSize = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        (ICommand) () -> { throw new Exception("test exception"); },
                        (ICommand) () -> System.out.println("command3"),
                        (ICommand) () -> System.out.println("command4"),
                        new CountDownCommand(countDownLatch)
                )
        );
        ServerThread serverThread = new ServerThread(blockingQueue);

        //When
        serverThread.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, serverThread.getQueueSize());
    }

    @SneakyThrows
    @Test
    void should_execute_hard_stop() {
        //Given
        int resultSize = 2;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ServerThread serverThread = new ServerThread();
        BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        new MacroCommand(new ICommand[]{new HardStopCommand(serverThread), new CountDownCommand(countDownLatch)}),
                        (ICommand) () -> System.out.println("command3"),
                        (ICommand) () -> System.out.println("command4")
                )
        );
        serverThread.setBlockingQueue(blockingQueue);

        //When
        serverThread.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, serverThread.getQueueSize());
    }

    @SneakyThrows
    @Test
    void should_execute_soft_stop() {
        //Given
        int resultSize = 0;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ServerThread serverThread = new ServerThread();
        BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(10);
        blockingQueue.addAll(
                List.of(
                        (ICommand) () -> System.out.println("command1"),
                        (ICommand) () -> System.out.println("command2"),
                        new SoftStopCommand(serverThread, countDownLatch),
                        (ICommand) () -> System.out.println("command3"),
                        (ICommand) () -> System.out.println("command4")
                )
        );
        serverThread.setBlockingQueue(blockingQueue);
        serverThread.addCommand(() -> System.out.println("command5"));

        //When
        serverThread.start();
        countDownLatch.await();

        //Then
        assertEquals(resultSize, serverThread.getQueueSize());
    }
}
