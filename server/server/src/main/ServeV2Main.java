package main;

import beans.BeanManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.Configs;
import config.DataSourceConfig;
import config.InitConfig;
import hanlder.*;
import meta.service.IDatabaseStatusService;
import meta.service.impl.DatabaseStatusServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import rest.Controller;
import rest.UriPatternMatcher;
import server.Server;
import task.LogOnStart;
import task.MultiTask;
import task.ShutdownServer;
import util.ClasspathHacker;
import util.FileUtil;
import util.loader.CustomClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
                setupDataSource();
                setupServer();
                setupMetaApis();
                setupApis();
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

        URL url = null;
        try {
            url = new File(configs.getClasspath()).toURI().toURL();
        } catch (MalformedURLException e) {
            logger.catching(e);
        }

        CustomClassLoader customClassLoader = new CustomClassLoader(
                new URL[]{url},
                ClassLoader.getSystemClassLoader()
        );

        Configs.setConfigs(Configs.CLASSLOADER, customClassLoader);
    }

    private static void setupDataSource() {
        DataSourceConfig dataSource = null;

        IDatabaseStatusService databaseStatusService = new DatabaseStatusServiceImpl();
        File activeCfgFile = databaseStatusService.activeCfgFile();
        if (null != activeCfgFile) {
            dataSource = FileUtil.getObjectFromFile(activeCfgFile, DataSourceConfig.class);
        }

        if (null == dataSource) {
            InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
            dataSource = initConfig.getDataSource();
        }

        DatasourceFactory.newDataSource(
                dataSource.getUrl(),
                dataSource.getUser(),
                dataSource.getPwd()
        );
    }

    private static void setupServer() {
        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);

        Server server = new Server();
        server.addHandler("*", new FallbackHandler());
        server.addHandler("/shutdown/*", new ShutdownHandler(server));
        server.addHandler("/upload/*", new UploadHandler(
                initConfig.getUploadRoot(),
                "/upload"
        ));

        List<Controller> metaApis = new ArrayList<>();
        server.addHandler("/metaApi/*", new ApiHandler(metaApis));
        Configs.setConfigs(Configs.META_APIS, metaApis);

        List<Controller> apis = new ArrayList<>();
        server.addHandler("/api/*", new ApiHandler(apis));
        Configs.setConfigs(Configs.APIS, apis);

        server.addHandler("/deploy/*", new HttpFileHandler(
                initConfig.getDeployRoot(), "/deploy"
        ));

        server.addHandler("/serve/*", new HttpFileHandler(
                initConfig.getResourceRoot(), "/serve"
        ));

        server.start(new MultiTask().addTask(new LogOnStart("startup.log")));
    }

    private static void setupMetaApis() {
        List<Controller> metaApis = Configs.getConfigs(Configs.META_APIS, List.class);

        Enumeration<URL> dirs = null;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources("meta");
        } catch (IOException e) {
            logger.catching(e);
            return;
        }

        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();

            if (!"jar".equals(url.getProtocol())) {
                continue;
            }

            JarFile jarFile = null;
            try {
                jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
            } catch (IOException e) {
                logger.catching(e);
                continue;
            }

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                String name = jarEntry.getName();
                if (name.charAt(0) == File.separatorChar) {
                    name = name.substring(1);
                }

                if (name.startsWith(String.join(File.separator, "meta", "controller"))
                        && name.endsWith(".class")
                        && !jarEntry.isDirectory()) {
                    String className = name.replaceAll(File.separator, ".");
                    className = className.substring(0, className.length() - ".class".length());

                    try {
                        Controller controller = (Controller) Class.forName(className).newInstance();
                        UriPatternMatcher uriPatternMatcher = new UriPatternMatcher("/metaApi");
                        uriPatternMatcher.setController(controller);
                        controller.setUriPatternMatcher(uriPatternMatcher);
                        metaApis.add(controller);
                    } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        logger.catching(e);
                    }
                }
            }
        }
    }

    public static void setupApis() {
        List<Controller> apis = Configs.getConfigs(Configs.APIS, List.class);
        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);

        BeanManager.getInstance().setController(
                (Class<? extends Controller>) classLoader.loadClass("controller.TestController")
        );
        Controller controller = BeanManager.getInstance().getController("controller.TestController");
        UriPatternMatcher uriPatternMatcher = new UriPatternMatcher("/api");
        uriPatternMatcher.setController(controller);
        controller.setUriPatternMatcher(uriPatternMatcher);
        apis.add(controller);
    }

}
