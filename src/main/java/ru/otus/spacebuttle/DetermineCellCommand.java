package ru.otus.spacebuttle;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

@RequiredArgsConstructor
public class DetermineCellCommand implements ICommand {
    private final List<Context> contexts;
    private final UObject uObject;
    private final BlockingDeque<ICommand> blockingDeque;

    @Override
    public void execute() throws Exception {
        contexts.forEach(context -> {
            Vector position = (Vector) uObject.getProperty("Position");
            Map<Cell, Set<UObject>> locationContext = context.getLocationContext();
            locationContext.keySet()
                    .stream()
                    .filter(cell -> checkCell(cell.x1(), cell.x2(), cell.y1(), cell.y2(), position.x(), position.y()))
                    .findFirst()
                    .ifPresent((cell) -> {
                        updateCell(locationContext, cell, context);
                        makeCollisionCommands(locationContext, cell, context);
                    });
        });
    }

    private Boolean checkCell(Double x1, Double x2, Double y1, Double y2, Double x, Double y) {
        return x1 <= x && x < x2 && y1 <= y && y < y2;
    }

    private void updateCell(Map<Cell, Set<UObject>> locationContext, Cell cell, Context context) {
        final String property = "Cell_" + context.getTitle();
        Cell preCell = (Cell) uObject.getProperty(property);
        if (preCell != null)
            locationContext.get(preCell).remove(uObject);

        Set<UObject> uObjects = locationContext.get(cell);
        uObjects.add(uObject);

        uObject.setProperty(property, cell);
    }

    private void makeCollisionCommands(Map<Cell, Set<UObject>> locationContext, Cell cell, Context context) {
        Set<UObject> uObjects = locationContext.get(cell);
        List<ICommand> iCommands = new ArrayList<>();
        uObjects.stream()
                .filter(uo -> uObject != uo)
                .forEach(uo -> iCommands.add(new CheckCollisionCommand(context, uObject, uo)));
        MacroCommand macroCommand = new MacroCommand(iCommands.toArray(new ICommand[0]));
        blockingDeque.addFirst(macroCommand);
    }
}
