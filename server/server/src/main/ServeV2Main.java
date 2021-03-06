package main;

import auth.AuthHelper;
import auth.DefaultServeAuth;
import beans.BeanManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import config.Configs;
import config.DataSourceConfig;
import config.InitConfig;
import hanlder.*;
import meta.data.EIConfig;
import meta.repository.IConfigRepo;
import meta.service.IAuthService;
import meta.service.IControllerService;
import meta.service.IDatabaseStatusService;
import meta.service.IServiceService;
import meta.service.impl.AuthServiceImpl;
import meta.service.impl.ControllerServiceImpl;
import meta.service.impl.DatabaseStatusServiceImpl;
import meta.service.impl.ServiceServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import rest.Controller;
import rest.UriPatternMatcher;
import server.Server;
import task.LogOnStart;
import task.MultiTask;
import task.ShutdownServer;
import util.FileUtil;
import util.loader.CustomClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServeV2Main {

    private static Logger logger = LogManager.getLogger(ServeV2Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("参数不正确！");
            return;
        }

        setFileRoot();
        setObjectMapper();

        switch (args[0]) {
            case "start":
                startServer();
                break;
            case "stop":
                stopServer();
                break;
            case "restart":
                restartServer(Integer.parseInt(args[1]));
                break;
            default:
                System.err.println(String.format("未识别参数 : %s！", args[0]));
        }
    }

    private static void restartServer(int seconds) {
        stopServer();
        try {
            TimeUnit.SECONDS.sleep(Math.max(10, seconds));
            startServer();
        } catch (InterruptedException e) {
            logger.catching(e);
        }
    }

    private static void stopServer() {
        try {
            new ShutdownServer("startup.log").call();
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    private static void startServer() {
        logger.info(AuthHelper.ADMIN + " is what so called admin.");
        readInitConfigs();
        boolean withSchema = setupDataSource();
        Configs.setConfigs(Configs.WITH_SCHEMA, withSchema);
        setupServeAuth(withSchema);
        setupAuth(withSchema);
        setupServer();
        setupMetaApis();
        setupApis(withSchema);
    }

    private static void setupAuth(boolean withSchema) {
        AuthHelper.init();
        if (!withSchema) {
            return;
        }

        BeanManager.getInstance().setService(IAuthService.class, AuthServiceImpl.class);
        IAuthService authService = BeanManager.getInstance().getService(IAuthService.class);
        authService.initAuths();
    }

    private static void setupServeAuth(boolean withSchema) {
        if (!withSchema) {
            Configs.setConfigs(Configs.SERVE_AUTH, new DefaultServeAuth());
            return;
        }

        IConfigRepo configRepo = BeanManager.getInstance().getRepository(IConfigRepo.class);

        if (!configRepo.createTableIfNotExist()) {
            logger.error("无法创建配置表");
            return;
        }

        EIConfig config = configRepo.findByConfigKey("serve.auth");
        if (null == config || "".equals(config.getConfigValue())) {
            Configs.setConfigs(Configs.SERVE_AUTH, new DefaultServeAuth());
            return;
        }

        CustomClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, CustomClassLoader.class);

        try {
            Configs.setConfigs(Configs.SERVE_AUTH, classLoader.loadClass(config.getConfigValue()).newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            logger.catching(e);
            Configs.setConfigs(Configs.SERVE_AUTH, new DefaultServeAuth());
        }
    }

    private static void setFileRoot() {
        String filepath = ServeV2Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        Configs.setConfigs(Configs.JAR_FILE_PATH, filepath);
        filepath = filepath.substring(0, filepath.lastIndexOf(File.separator) + 1);
        Configs.setConfigs(Configs.FILE_ROOT, filepath);
    }

    private static void setObjectMapper() {
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        OBJECT_MAPPER.setDateFormat(dateFormat);

        Configs.setConfigs(Configs.DATE_FORMAT, dateFormat);
        Configs.setConfigs(Configs.OBJECT_MAPPER, OBJECT_MAPPER);
    }

    private static void readInitConfigs() {
        InitConfig configs = FileUtil.getObjectFromFile(
                new File(Configs.getConfigs(Configs.FILE_ROOT, String.class), "serveV2.json"),
                InitConfig.class);
        Configs.setConfigs(InitConfig.CONFIG_KEY, configs);

        URL url = null;
        try {
            assert configs != null;
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

    private static boolean setupDataSource() {
        boolean ret = false;

        DataSourceConfig dataSource = null;

        IDatabaseStatusService databaseStatusService = new DatabaseStatusServiceImpl();
        File activeCfgFile = databaseStatusService.activeCfgFile();
        if (null != activeCfgFile) {
            dataSource = FileUtil.getObjectFromFile(activeCfgFile, DataSourceConfig.class);
            ret = true;
        }

        if (null == dataSource) {
            InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
            dataSource = initConfig.getDataSource();
            ret = false;
        }

        DatasourceFactory.newDataSource(
                dataSource.getUrl(),
                dataSource.getUser(),
                dataSource.getPwd()
        );

        return ret;
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
                initConfig.getDeployRoot(), "/deploy", "index.html"
        ));

        server.addHandler("/serve/*", new ServeHandler(
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

    private static void setupApis(boolean withSchema) {
        if (!withSchema) {
            return;
        }

        BeanManager.getInstance().setService(IControllerService.class, ControllerServiceImpl.class);
        BeanManager.getInstance().setService(IServiceService.class, ServiceServiceImpl.class);

        IControllerService controllerService = BeanManager.getInstance().getService(IControllerService.class);
        IServiceService serviceService = BeanManager.getInstance().getService(IServiceService.class);

        try {
            serviceService.initAllService();
            controllerService.initAllControllers();
        } catch (Throwable e) {
            logger.catching(e);
        }
    }

}
