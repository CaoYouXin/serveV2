package util.loader;

import config.Configs;
import config.InitConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.FileUtil;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class CustomClassLoader extends URLClassLoader {

    private static final Logger logger = LogManager.getLogger(CustomClassLoader.class);

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

        for (int i = 0; i < bytes.length; i++) {
            if (cachedBytes[i] != bytes[i]) {
                return false;
            }
        }
        return true;
    }

    public Class<?> defineClass1(String name, byte[] bytes, int offset, int length) {
        return super.defineClass(name, bytes, offset, length);
    }

    @Override
    public Class<?> loadClass(String name) {
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            logger.catching(e);
            return null;
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        System.out.println("ccl : " + name);

        String path = name.replaceAll("\\.", File.separator).concat(".class");
        URL resource = this.findResource(path);
        if (null == resource) {
            return super.loadClass(name, resolve);
        }

//        System.out.println("url : " + resource);

        if (!resource.getProtocol().equals("file")) {
            return super.loadClass(name, resolve);
        }

        if (!resource.getPath().startsWith(Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class).getClasspath())) {
            return super.loadClass(name, resolve);
        }

        byte[] bytesFromURL = FileUtil.getBytesFromURL(resource);
        if (null == bytesFromURL) {
            return super.loadClass(name, resolve);
        }

        if (this.isCached(name, bytesFromURL)) {
            return this.cached.get(name);
        }

//        System.out.println("not cache");

        LocalClassLoader localClassLoader = new LocalClassLoader(this);
        Class<?> aClass = localClassLoader.defineClass(name, bytesFromURL);

        if (aClass.getPackage() == null) {
            int lastDotIndex = name.lastIndexOf('.');
            String packageName = (lastDotIndex >= 0) ? name.substring(0, lastDotIndex) : "";
            definePackage(packageName, null, null, null, null, null, null, null);
        }

        if (resolve) {
            resolveClass(aClass);
        }

        this.cache.put(name, bytesFromURL);
        this.cached.put(name, aClass);
        return aClass;
    }
}
