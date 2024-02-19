package ru.otus.spacebuttle.core;

import lombok.Setter;
import lombok.SneakyThrows;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.scope.BehaviourState;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class ServerThreadNew {
    private final Thread thread;
    @Setter
    private BehaviourState behaviourState;
    private Boolean isStop;

    @SneakyThrows
    public ServerThreadNew(BehaviourState behaviourState) {
        this.thread = threadInitialize();
        this.behaviourState = behaviourState;
        this.isStop = false;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        isStop = true;
    }

    public void updateBehaviour(Consumer<BlockingQueue<ICommand>> behaviour) {
        this.behaviourState.setBehaviour(behaviour);
    }

    private Thread threadInitialize() {
        return new Thread(() -> {
            while (!isStop) {
                behaviourState.handle();
                if (behaviourState == null)
                    stop();
            }
        });
    }
}
