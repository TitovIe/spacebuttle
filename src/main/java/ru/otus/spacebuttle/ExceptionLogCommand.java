package ru.otus.spacebuttle;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionLogCommand implements ICommand {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ExceptionLogCommand.class);
    private static final String MESSAGE = "Exception was thrown while processing";
    private final Exception exception;

    public ExceptionLogCommand(Exception exception) {
        this.exception = exception;
    }

    @Override
    public void execute() {
        LOGGER.error(MESSAGE, exception);
    }
}
