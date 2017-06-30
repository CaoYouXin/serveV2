package loader;

import org.junit.jupiter.api.Test;
import util.loader.CustomClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

class LoaderTest {

    @Test
    void test() {
        URL url = null;
        try {
            url = new File("/Users/cls/Dev/Git/personal/infinitely/serveV2/out/classpath/").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        CustomClassLoader customClassLoader = new CustomClassLoader(
                new URL[]{url},
                ClassLoader.getSystemClassLoader()
        );
        try {
            System.out.println(customClassLoader.loadClass("service.impl.TestService1").newInstance());

            System.out.println(customClassLoader.loadClass("service.impl.TestService1").equals(
                    customClassLoader.loadClass("service.impl.TestService1")
            ));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void print() {
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        System.out.println(Arrays.toString(urlClassLoader.getURLs()));
    }

}
