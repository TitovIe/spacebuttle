package ru.otus.spacebuttle.scope;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class DependencyResolver implements IDependencyResolver {
    private final Map<String, Function<Object[], Object>> dependencies;

    public DependencyResolver(Object dependencies) {
        this.dependencies = (Map<String, Function<Object[], Object>>) dependencies;
    }

    @Override
    public Object resolve(String dependency, Object... args) {
        Map<String, Function<Object[], Object>> dependencies = this.dependencies;

        Function<Object[], Object> dependencyResolverStrategy;
        while (true) {
            dependencyResolverStrategy = dependencies.get(dependency);
            if (Objects.nonNull(dependencyResolverStrategy)) {
                return dependencyResolverStrategy.apply(args);
            }
            else {
                dependencies = (Map<String, Function<Object[], Object>>) dependencies.get("IoC.Scope.Parent").apply(args);
                if (Objects.isNull(dependencies)) {
                    throw new DependencyResolverException("The root scope has no a parent scope.");
                }
            }
        }
    }
}
