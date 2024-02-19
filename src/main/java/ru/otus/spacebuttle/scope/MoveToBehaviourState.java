package ru.otus.spacebuttle.scope;

import lombok.Getter;
import lombok.Setter;
import ru.otus.spacebuttle.ICommand;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class MoveToBehaviourState implements BehaviourState {
    @Getter
    private final BlockingQueue<ICommand> blockingQueue;
    @Getter
    private final BlockingQueue<ICommand> blockingQueueTo;
    @Setter
    private Consumer<BlockingQueue<ICommand>> behaviour;

    public MoveToBehaviourState(BlockingQueue<ICommand> blockingQueue,
                                BlockingQueue<ICommand> blockingQueueTo,
                                Consumer<BlockingQueue<ICommand>> behaviour) {
            this.blockingQueue = blockingQueue;
            this.blockingQueueTo = blockingQueueTo;
            this.behaviour = behaviour;
    }

    @Override
    public void handle() {
        blockingQueue.drainTo(blockingQueueTo);
        behaviour.accept(blockingQueueTo);
    }
}
