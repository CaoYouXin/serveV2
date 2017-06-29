package task;

import config.Configs;
import config.InitConfig;
import config.StartLog;
import util.FileUtil;
import util.NetUtil;
import util.StringUtil;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.util.concurrent.Callable;

public class LogOnStart implements Callable<Void> {

    private File file;

    public LogOnStart(String fileName) {
        this.file = new File(Configs.getConfigs(Configs.FILE_ROOT, String.class), fileName);
    }

    @Override
    public Void call() throws Exception {
        StartLog log = new StartLog();
        log.setHost(NetUtil.getLocalHostLANAddress().getHostAddress());
        log.setPort(Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class).getPort());
        log.setToken(StringUtil.getMD5(Double.toString(Math.random())));
        FileUtil.writeObjectToFile(log, this.file);
        Configs.setConfigs(StartLog.CONFIG_KEY, log.getToken());
        return null;
    }
}
