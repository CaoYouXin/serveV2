package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathHacker {
    private static final Logger logger = LogManager.getLogger(ClasspathHacker.class);

    private static final Class<?>[] addURLParameters = new Class[]{URL.class};
    private static final Class<?>[] loadClassParameters = new Class[]{String.class, boolean.class};

    public static void addFile(String s) {
        File f = new File(s);
        addFile(f);
    }

    public static void addFile(File f) {
        try {
            addURL(f.toURI().toURL());
        } catch (MalformedURLException e) {
            logger.catching(e);
        }
    }

    public static void addURL(URL u) {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> urlClassLoaderClass = URLClassLoader.class;
        try {
            Method method = urlClassLoaderClass.getDeclaredMethod("addURL", addURLParameters);
            method.setAccessible(true);
            method.invoke(systemClassLoader, u);
        } catch (Throwable t) {
            logger.catching(t);
            throw new RuntimeException("Error, could not add URL to system classloader");
        }
    }

    public static Class<?> loadClass(String className, boolean resolve) {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> classLoaderClass = ClassLoader.class;
        try {
            Method method = classLoaderClass.getDeclaredMethod("loadClass", loadClassParameters);
            method.setAccessible(true);
            return (Class<?>) method.invoke(systemClassLoader, className, resolve);
        } catch (Throwable t) {
            logger.catching(t);
            throw new RuntimeException("Error, could not add URL to system classloader");
        }
    }

    public static URLClassLoader setupClassLoader(String[] dirs) {
        URL[] urls = new URL[dirs.length];
        try {
            for (int i=0; i<dirs.length; i++) {
                urls[i] = new File(dirs[i]).toURI().toURL();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
    }
}
