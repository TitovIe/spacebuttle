package ru.otus.spacebuttle.scope;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spacebuttle.ICommand;
import ru.otus.spacebuttle.core.Ioc;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class IocTest {

    @SneakyThrows
    @BeforeEach
    void setup() {
        new InitCommand().execute();
        var iocScope = Ioc.resolve("IoC.Scope.Create");
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();
    }

    @SneakyThrows
    @Test
    void resolve_should_resolve_when_registered_dependency_in_currentScope() {
        //Given
        ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency", (Function<Object[], Object>) (Object[] args) -> (Object) 1)).execute();

        //When
        //Then
        assertEquals(1, (Integer) Ioc.resolve("registeredDependency"));
    }

    @Test
    void resolve_should_throw_exception_when_unregistered_dependency_in_currentScope() {
        //Given
        //When
        //Then
        assertThrows(DependencyResolverException.class, () -> Ioc.resolve("registeredDependency"));
    }

    @SneakyThrows
    @Test
    void resolve_should_use_parent_scope_when_resolving_dependency_is_not_defined_in_current_scope() {
        //Given
        ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency", (Function<Object[], Object>) (Object[] args) -> (Object) 1)).execute();

        //When
        var iocScope = Ioc.resolve("IoC.Scope.Create");
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();

        //Then
        assertEquals(iocScope, Ioc.resolve("IoC.Scope.Current"));
        assertEquals(1, (Integer) Ioc.resolve("registeredDependency"));
    }

    @SneakyThrows
    @Test
    void resolve_should_use_parent_scope_when_set_manually_for_creating_scope()
    {
        var scope1 = Ioc.resolve("IoC.Scope.Create");
        var scope2 = Ioc.resolve("IoC.Scope.Create", scope1);

        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", scope1)).execute();
        ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency", (Function<Object[], Object>) (Object[] args) -> (Object) 2)).execute();
        ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", scope2)).execute();

        assertEquals(2, (Integer) Ioc.resolve("registeredDependency"));
    }

    @SneakyThrows
    @Test
    void resolve_should_resolve_in_threads_scope_when_registered_dependency_in_threads_scope() {
        //Given
        Runnable task1 = () -> {
            try {
                var iocScope = Ioc.resolve("IoC.Scope.Create");
                ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();
                ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency1", (Function<Object[], Object>) (Object[] args) -> (Object) 1)).execute();
                //When
                //Then
                assertEquals(1, (Integer) Ioc.resolve("registeredDependency1"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable task2 = () -> {
            try {
                var iocScope = Ioc.resolve("IoC.Scope.Create");
                ((ICommand) Ioc.resolve("IoC.Scope.Current.Set", iocScope)).execute();
                ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency2", (Function<Object[], Object>) (Object[] args) -> (Object) 2)).execute();
                //When
                //Then
                assertThrows(DependencyResolverException.class, () -> Ioc.resolve("registeredDependency1"));
                assertEquals(2, (Integer) Ioc.resolve("registeredDependency2"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        ((ICommand) Ioc.resolve("IoC.Register", "registeredDependency3", (Function<Object[], Object>) (Object[] args) -> (Object) 3)).execute();

        //When
        thread1.start();
        thread2.start();

        //Then
        assertThrows(DependencyResolverException.class, () -> Ioc.resolve("registeredDependency1"));
        assertThrows(DependencyResolverException.class, () -> Ioc.resolve("registeredDependency2"));
        assertEquals(3, (Integer) Ioc.resolve("registeredDependency3"));
    }
}
