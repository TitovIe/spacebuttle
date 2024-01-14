package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FinishCommand implements ICommand {
    private final UObject uObject;

    @Override
    public void execute() throws Exception {
        log.info("Finish was called for object with position {} and velocity {} !",
                uObject.getProperty("Position"), uObject.getProperty("Velocity"));
    }
}
