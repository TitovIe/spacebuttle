package ru.otus.spacebuttle.generator;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.util.HashMap;
import java.util.Map;

public class InMemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private Map<String, JavaClassAsBytes> compiledClasses;
    private ClassLoader loader;

    public InMemoryFileManager(StandardJavaFileManager standardManager) {
        super(standardManager);
        this.compiledClasses = new HashMap<>();
        this.loader = new InMemoryClassLoader(this.getClass().getClassLoader(), this);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) {
        JavaClassAsBytes classAsBytes = new JavaClassAsBytes(className, kind);
        compiledClasses.put(className, classAsBytes);

        return classAsBytes;
    }

    public Map<String, JavaClassAsBytes> getBytesMap() {
        return compiledClasses;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return loader;
    }
}