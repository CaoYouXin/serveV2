package task;

import config.Configs;
import config.StartLog;
import util.FileUtil;
import util.NetUtil;

import java.io.File;
import java.util.concurrent.Callable;

public class ShutdownServer implements Callable<Void> {

    private StartLog log;

    public ShutdownServer(String fileName) {
        this.log = FileUtil.getObjectFromFile(new File(
                Configs.getConfigs(Configs.FILE_ROOT, String.class, null), fileName
        ), StartLog.class);
    }

    @Override
    public Void call() throws Exception {
        NetUtil.getWithoutResponse(String.format("http://%s:%d/shutdown/%s",
                this.log.getHost(), this.log.getPort(), this.log.getToken()));

//        BashUtil.run("kill -9 ${lsof -t -i:" + this.log.getPort() + "}", false);
        return null;
    }
}
