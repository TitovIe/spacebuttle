package ru.otus.spacebuttle.scope;

import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.Ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InitCommand implements ICommand {
    public static ThreadLocal<Object> currentScope = new ThreadLocal<>();
    private static Map<String, Function<Object[], Object>> rootScope = new ConcurrentHashMap<>();
    private static boolean executedSuccessfully = false;

    @Override
    public void execute() throws Exception {
        if (executedSuccessfully)
            return;

        rootScope.putIfAbsent(
                "IoC.Scope.Current.Set",
                (Object[] args) -> new SetCurrentScopeCommand(args[0]));

        rootScope.putIfAbsent(
                "IoC.Scope.Current.Clear",
                (Object[] args) -> new ClearCurrentScopeCommand());

        rootScope.putIfAbsent(
                "IoC.Scope.Current",
                (Object[] args) -> currentScope.get() != null ? currentScope.get() : rootScope);

        rootScope.putIfAbsent(
                "IoC.Scope.Parent",
                (Object[] args) -> null);

        rootScope.putIfAbsent(
                "IoC.Scope.Create.Empty",
                (Object[] args) -> new ConcurrentHashMap<String, Function<Object[], Object>>());

        rootScope.putIfAbsent(
                "IoC.Scope.Create",
                (Object[] args) ->
                {
                    Map<String, Function<Object[], Object>> creatingScope = Ioc.resolve("IoC.Scope.Create.Empty");

                    if (args.length > 0) {
                        Object parentScope = args[0];
                        creatingScope.put("IoC.Scope.Parent", (Object[] args_) -> parentScope);
                    } else {
                        Object parentScope = Ioc.resolve("IoC.Scope.Current");
                        creatingScope.put("IoC.Scope.Parent", (Object[] args_) -> parentScope);
                    }
                    return creatingScope;
                }
        );

        rootScope.putIfAbsent(
                "IoC.Register",
                (Object[] args) -> new RegisterDependencyCommand((String) args[0], (Function<Object[], Object>) args[1])
        );

        ((ICommand) Ioc.resolve("Update Ioc Resolve Dependency Strategy",
                (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>)
                        (BiFunction<String, Object[], Object> oldStrategy) ->
                                (String dependency, Object[] args) -> {
                                    final var scope = currentScope.get() != null ? currentScope.get() : rootScope;
                                    final var dependencyResolver = new DependencyResolver(scope);

                                    return dependencyResolver.resolve(dependency, args);
                                })).execute();

        executedSuccessfully = true;
    }
}
