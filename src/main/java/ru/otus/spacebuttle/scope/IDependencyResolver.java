package ru.otus.spacebuttle.scope;

public interface IDependencyResolver {
    Object resolve(String dependency, Object... args);
}
