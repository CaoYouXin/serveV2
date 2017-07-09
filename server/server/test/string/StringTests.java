package string;

import org.junit.jupiter.api.Test;

import java.io.File;

class StringTests {

    @Test
    void testFileSep() {
        System.out.println(File.separator);
    }

    @Test
    void replaceAllTest() {
        System.out.println("?to=".replaceAll("\\?", "\\\\?"));
    }

}
