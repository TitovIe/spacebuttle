package ru.otus.spacebuttle.scope;

import ru.otus.spacebuttle.ICommand;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public interface BehaviourState {
    void handle();
    void setBehaviour(Consumer<BlockingQueue<ICommand>> behaviour);
}
