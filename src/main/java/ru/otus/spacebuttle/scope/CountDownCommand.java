package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import ru.otus.spacebuttle.ICommand;

import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class CountDownCommand implements ICommand {
    private final CountDownLatch countDownLatch;

    @Override
    public void execute() throws Exception {
        countDownLatch.countDown();
    }
}
