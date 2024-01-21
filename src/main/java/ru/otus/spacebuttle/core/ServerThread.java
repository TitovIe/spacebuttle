package ru.otus.spacebuttle.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spacebuttle.ExceptionHandler;
import ru.otus.spacebuttle.ICommand;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

@Slf4j
public class ServerThread {
    private final Thread thread;
    @Getter
    @Setter
    private BlockingQueue<ICommand> blockingQueue = new ArrayBlockingQueue<>(10);
    private Consumer<BlockingQueue<ICommand>> behaviour = (blockingQueue) -> {
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
    private Boolean isStop;

    public ServerThread() {
        this.thread = threadInitialize();
        this.isStop = false;
    }

    public ServerThread(BlockingQueue<ICommand> blockingQueue) {
        this.blockingQueue = blockingQueue;
        this.thread = threadInitialize();
        this.isStop = false;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        isStop = true;
    }

    public void updateBehaviour(Consumer<BlockingQueue<ICommand>> behaviour) {
        this.behaviour = behaviour;
    }

    public void addCommand(ICommand command) {
        this.blockingQueue.add(command);
    }

    public int getQueueSize() {
        return this.blockingQueue.size();
    }

    private Thread threadInitialize() {
        return new Thread(() -> {
            while (!isStop) {
                behaviour.accept(this.blockingQueue);
            }
        });
    }
}
