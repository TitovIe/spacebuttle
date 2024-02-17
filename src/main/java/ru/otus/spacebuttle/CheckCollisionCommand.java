package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CheckCollisionCommand implements ICommand {
    private final Context context;
    private final UObject uObject1;
    private final UObject uObject2;

    @Override
    public void execute() throws Exception {
        final Double condition = context.getCondition();
        final Vector position1 = (Vector) uObject1.getProperty("Position");
        final Vector position2 = (Vector) uObject2.getProperty("Position");
        boolean isCollision = Math.abs(position1.x() - position2.x()) < condition && Math.abs(position1.y() - position2.y()) < condition;
        if (isCollision)
            throw new IllegalStateException("Collision between " + uObject1 + " and " + uObject2);
    }
}
