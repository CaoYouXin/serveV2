package util.loader;

import util.FileUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

public class CustomClassLoader extends URLClassLoader {

    private Map<String, byte[]> cache = new HashMap<>();
    private Map<String, Class<?>> cached = new HashMap<>();

    public CustomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    private boolean isCached(String name, byte[] bytes) {
        byte[] cachedBytes = this.cache.get(name);

        if (null == cachedBytes) {
            return false;
        }

        if (bytes.length != cachedBytes.length) {
            return false;
        }

        for (int i=0; i<bytes.length; i++) {
            if (cachedBytes[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        String path = name.replaceAll("\\.", File.separator).concat(".class");
        URL resource = this.findResource(path);
        if (null == resource) {
            return super.loadClass(name, resolve);
        }

        byte[] bytesFromURL = FileUtil.getBytesFromURL(resource);
        if (null == bytesFromURL) {
            return super.loadClass(name, resolve);
        }

        if (this.isCached(name, bytesFromURL)) {
            return this.cached.get(name);
        }

        LocalClassLoader localClassLoader = new LocalClassLoader(this);
        Class<?> aClass = localClassLoader.defineClass(name, bytesFromURL);

        if (aClass.getPackage() == null) {
            int lastDotIndex = name.lastIndexOf( '.' );
            String packageName = (lastDotIndex >= 0) ? name.substring( 0, lastDotIndex) : "";
            definePackage( packageName, null, null, null, null, null, null, null );
        }

        if (resolve) {
            resolveClass( aClass );
        }

        this.cache.put(name, bytesFromURL);
        this.cached.put(name, aClass);
        return aClass;
    }
}
