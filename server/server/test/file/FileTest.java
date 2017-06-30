package file;

import org.junit.jupiter.api.Test;
import util.FileUtil;

import java.io.File;

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

}
