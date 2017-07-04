package file;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

class FileTest {

    @Test
    void testNewFile1() {
        File file = new File("/Users/cls/Dev/Git/personal/infinitely/serveV2/out/artifacts/serveV2/", "/startup.log");
        String content = FileUtil.getContent(file);
        System.out.println(content);
    }

    @Test
    void testNewFile2() {
        File file = new File("/Users/cls/Dev/Git/personal/infinitely/serveV2/out/artifacts/", "/serveV2/startup.log");
        String content = FileUtil.getContent(file);
        System.out.println(content);
    }

    @Test
    void testFileCopy() {
        File file = new File(
                "/Users/cls/Dev/Git/personal/infinitely/serveV2/out/test/", "test.json"
        );

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileWriter output = new FileWriter(file);
            IOUtils.copy(new StringReader("abc"), output);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
