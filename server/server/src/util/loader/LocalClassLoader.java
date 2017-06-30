package util.loader;

public class LocalClassLoader extends ClassLoader {

    public LocalClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> defineClass(String name, byte[] content) {
        return this.defineClass(name, content, 0, content.length);
    }

}
