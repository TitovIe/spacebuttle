package ru.otus.spacebuttle;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import ru.otus.spacebuttle.log.MemoryAppender;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {
    @Mock
    IMovable iMovable;
    @Mock
    IRotable iRotable;
    @InjectMocks
    MoveCommand moveCommand;
    @InjectMocks
    RotateCommand rotateCommand;
    private static BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(10);
    private static MemoryAppender memoryAppender;
    private static Logger logger;

    @BeforeAll
    static void setup() {
        logger = (Logger) LoggerFactory.getLogger(ExceptionLogCommand.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.ERROR);
        logger.addAppender(memoryAppender);

        ExceptionHandler.registerHandler(MoveCommand.class, IllegalArgumentException.class, (command, exception) -> () -> blockingQueue.add(new RepeatCommand(command)));
        ExceptionHandler.registerHandler(MoveCommand.class, NullPointerException.class, (command, exception) -> () -> blockingQueue.add(new ExceptionLogCommand(exception)));
        ExceptionHandler.registerHandler(MoveCommand.class, MoveException.class, (command, exception) -> () -> blockingQueue.add(new RepeatCommand(command)));
        ExceptionHandler.registerHandler(RepeatCommand.class, MoveException.class, (command, exception) -> () -> blockingQueue.add(new RepeatDoubleCommand(command)));
        ExceptionHandler.registerHandler(RepeatDoubleCommand.class, MoveException.class, (command, exception) -> () -> blockingQueue.add(new ExceptionLogCommand(exception)));
        ExceptionHandler.registerHandler(RotateCommand.class, RotateException.class, (command, exception) -> () -> blockingQueue.add(new RepeatCommand(command)));
        ExceptionHandler.registerHandler(RepeatCommand.class, RotateException.class, (command, exception) -> () -> blockingQueue.add(new ExceptionLogCommand(exception)));
    }

    @AfterEach
    void tearDown() {
        blockingQueue.clear();
        memoryAppender.reset();
    }

    @SneakyThrows
    @Test
    void execute_should_log_error_when_called() {
        memoryAppender.start();

        //Given
        Exception exception = new IllegalArgumentException();
        ICommand exceptionLogCommand = new ExceptionLogCommand(exception);

        //When
        exceptionLogCommand.execute();

        //Then
        assertTrue(memoryAppender.contains("Exception was thrown", Level.ERROR));

        memoryAppender.reset();
    }

    @SneakyThrows
    @Test
    void handle_log_should_add_in_queue_when_called() {
        //Given
        //When
        ExceptionHandler.handle(new NullPointerException(), moveCommand).execute();
        //Then
        assertEquals(1, blockingQueue.size());
        assertInstanceOf(ExceptionLogCommand.class, blockingQueue.take());
    }

    @SneakyThrows
    @Test
    void execute_should_repeat_execute_when_called() {
        memoryAppender.start();

        //Given
        Exception exception = new IllegalArgumentException();
        ICommand exceptionLogCommand = new ExceptionLogCommand(exception);
        ICommand exceptionLogRepeatCommand = new RepeatCommand(exceptionLogCommand);

        //When
        exceptionLogRepeatCommand.execute();

        //Then
        assertTrue(memoryAppender.contains("Exception was thrown", Level.ERROR));
        assertEquals(1, memoryAppender.getSize());

        memoryAppender.reset();
    }

    @SneakyThrows
    @Test
    void handle_repeat_should_add_in_queue_when_called() {
        //Given
        //When
        ExceptionHandler.handle(new IllegalArgumentException(), moveCommand).execute();

        //Then
        assertEquals(1, blockingQueue.size());
        assertInstanceOf(RepeatCommand.class, blockingQueue.take());
    }

    @SneakyThrows
    @Test
    void handle_should_double_repeat_and_log_when_called() {
        memoryAppender.start();

        //Given
        int i = 0;
        blockingQueue.add(moveCommand);

        //When
        doThrow(new NullPointerException()).when(iMovable).getPosition();
        while (blockingQueue.size() != 0) {
            ICommand command = blockingQueue.take();
            try {
                command.execute();
            } catch (Exception exception) {
                ExceptionHandler.handle(exception, command).execute();
                i++;
            }
        }

        //Then
        assertEquals(i, 3);
        assertEquals(1, memoryAppender.getSize());
        assertTrue(memoryAppender.contains("Exception was thrown", Level.ERROR));

        memoryAppender.reset();
    }

    @SneakyThrows
    @Test
    void handle_should_repeat_and_log_when_called() {
        memoryAppender.start();

        //Given
        int i = 0;
        blockingQueue.add(rotateCommand);

        //When
        doThrow(new NullPointerException()).when(iRotable).setDirection(any());
        while (blockingQueue.size() != 0) {
            ICommand command = blockingQueue.take();
            try {
                command.execute();
            } catch (Exception exception) {
                ExceptionHandler.handle(exception, command).execute();
                i++;
            }
        }

        //Then
        assertEquals(i, 2);
        assertEquals(1, memoryAppender.getSize());
        assertTrue(memoryAppender.contains("Exception was thrown", Level.ERROR));

        memoryAppender.reset();
    }
}
