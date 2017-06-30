package server;

import config.Configs;
import config.InitConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private final ServerBootstrap serverBootstrap;
    private HttpServer server;

    public Server() {
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        this.serverBootstrap = ServerBootstrap.bootstrap()
                .setListenerPort(Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class).getPort())
                .setServerInfo("Infinitely Serve/2.1")
                .setSocketConfig(socketConfig)
                .setExceptionLogger(new StdErrorExceptionLogger());
    }

    public Server addHandler(String url, HttpRequestHandler handler) {
        this.serverBootstrap.registerHandler(url, handler);
        return this;
    }

    public void start(Callable<Void> voidCallable) {
        this.server = this.serverBootstrap.create();

        try {
            this.server.start();

            if (null != voidCallable) {
                voidCallable.call();
            }

//            this.server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.catching(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown(5);
        }));
    }

    public void shutdown(int howLong) {
        this.server.shutdown(howLong, TimeUnit.SECONDS);
        logger.info(String.format("server will shutdown in %d seconds...", howLong));
    }

}
