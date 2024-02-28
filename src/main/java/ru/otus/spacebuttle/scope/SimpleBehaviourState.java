package ru.otus.spacebuttle.scope;

import lombok.Getter;
import lombok.Setter;
import ru.otus.spacebuttle.ICommand;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class SimpleBehaviourState implements BehaviourState {
    @Getter
    private final BlockingQueue<ICommand> blockingQueue;
    @Setter
    private Consumer<BlockingQueue<ICommand>> behaviour;

    public SimpleBehaviourState(BlockingQueue<ICommand> blockingQueue,
                                Consumer<BlockingQueue<ICommand>> behaviour) {
        this.blockingQueue = blockingQueue;
        this.behaviour = behaviour;
    }

    @Override
    public void handle() {
        behaviour.accept(blockingQueue);
    }
}
