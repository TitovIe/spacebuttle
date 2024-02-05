package ru.otus.spacebuttle.scope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.spacebuttle.ExceptionHandler;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.ServerThread;

import java.util.concurrent.CountDownLatch;

@Slf4j
@RequiredArgsConstructor
public class SoftStopCommand implements ICommand {
    private final ServerThread serverThread;
    private final CountDownLatch countDownLatch;

    @Override
    public void execute() throws Exception {
        serverThread.updateBehaviour((blockingQueue) -> {
            if (blockingQueue.isEmpty()) {
                serverThread.stop();
                countDownLatch.countDown();
            } else {
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
            }
        });
    }
}
