package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.Configs;
import config.InitConfig;
import hanlder.FallbackHandler;
import hanlder.ShutdownHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;
import task.LogOnStart;
import task.MultiTask;
import task.ShutdownServer;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ServeV2Main {

    private static Logger logger = LogManager.getLogger(ServeV2Main.class);

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("参数不正确！");
            return;
        }

        setFileRoot();
        setObjectMapper();

        switch (args[0]) {
            case "start":
                readInitConfigs();
                setupServer();
                break;
            case "stop":
                try {
                    new ShutdownServer("startup.log").call();
                } catch (Exception e) {
                    logger.catching(e);
                }
                break;
            default:
                System.err.println(String.format("未识别参数 : %s！", args[0]));
        }
    }

    private static void setFileRoot() {
        String filepath = ServeV2Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        filepath = filepath.substring(0, filepath.lastIndexOf(File.separator) + 1);
        Configs.setConfigs(Configs.FILE_ROOT, filepath);
    }

    private static void setObjectMapper() {
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        OBJECT_MAPPER.setDateFormat(dateFormat);

        Configs.setConfigs(Configs.OBJECT_MAPPER, OBJECT_MAPPER);
    }

    private static void readInitConfigs() {
        InitConfig configs = FileUtil.getObjectFromFile(
                new File(Configs.getConfigs(Configs.FILE_ROOT, String.class), "serveV2.json"),
                InitConfig.class);
        Configs.setConfigs(InitConfig.CONFIG_KEY, configs);
    }

    private static void setupServer() {
        Server server = new Server();
        server.addHandler("*", new FallbackHandler());
        server.addHandler("/shutdown/*", new ShutdownHandler(server));
        server.start(new MultiTask().addTask(new LogOnStart("startup.log")));
    }

}
