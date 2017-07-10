package string;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

class StringTests {

    @Test
    void testFileSep() {
        System.out.println(File.separator);
    }

    @Test
    void replaceAllTest() {
        System.out.println("?to=".replaceAll("\\?", "\\\\?"));
    }

    @Test
    void regTest() {
        Pattern compile1 = Pattern.compile("^/metaApi/controller/set/(?<className>.*?)$");
        Pattern compile2 = Pattern.compile("^/metaApi/controller/set/(?<id>.*?)/(?<disabled>.*?)$");
        Pattern compile3 = Pattern.compile("^/metaApi/controller/set/(?<className>[^\\/\\?&]*?)$");
        Pattern compile4 = Pattern.compile("^/metaApi/controller/set/(?<id>.*?)/(?<disabled>[^\\/\\?&]*?)$");
        Pattern compile5 = Pattern.compile("/metaApi/controller/set/(?<className>[^\\/\\?&]*?)");
        Pattern compile6 = Pattern.compile("/metaApi/controller/set/(?<id>.*?)/(?<disabled>[^\\/\\?&]*?)");

        String url = "/metaApi/controller/set/1/T";

        System.out.println(compile1.matcher(url).matches());
        System.out.println(compile2.matcher(url).matches());
        System.out.println(compile3.matcher(url).matches());
        System.out.println(compile4.matcher(url).matches());
        System.out.println(compile5.matcher(url).matches());
        System.out.println(compile6.matcher(url).matches());
    }

}
