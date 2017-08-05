package task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;

public class FileCopy implements Callable<Boolean> {

    private final String src;
    private final String dst;

    public FileCopy(String src, String dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public Boolean call() throws Exception {
        File srcFile = new File(this.src);

        if (srcFile.isDirectory()) {

            boolean ret = true;
            for (File file : srcFile.listFiles()) {
                if (ret) {
                    ret = new FileCopy(
                            this.src + (this.hasSepInEnd(this.src) ? "" : File.separator) + file.getName(),
                            this.dst + (this.hasSepInEnd(this.dst) ? "" : File.separator) + file.getName()
                    ).call();
                }
            }
            return ret;

        }

        File dstFile = new File(this.dst + (this.hasSepInEnd(this.dst) ? "" : File.separator) + srcFile.getName());
        Files.copy(srcFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return true;
    }

    private boolean hasSepInEnd(String path) {
        return path.lastIndexOf(File.separatorChar) == path.length() - 1;
    }

}
