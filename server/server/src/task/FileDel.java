package task;

import java.io.File;
import java.util.concurrent.Callable;

public class FileDel implements Callable<Boolean> {

    private final File dir;

    public FileDel(File dir) {
        this.dir = dir;
    }

    @Override
    public Boolean call() throws Exception {
        return this.delete(this.dir);
    }

    private boolean delete(File dir) {
        if (!dir.exists()) {
            return true;
        }

        File[] files = dir.listFiles();
        if (null == files) {
            return dir.delete();
        }

        boolean ret = true;
        for (File file : files) {
            if (file.isDirectory()) {
                this.delete(file);
            } else {
                ret = ret && file.delete();
            }
        }
        return ret && dir.delete();
    }

}
