package hanlder;

import config.Configs;
import config.StartLog;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShutdownHandler implements HttpRequestHandler {

    private static final Logger logger = LogManager.getLogger(ShutdownHandler.class);
    private final Server server;
    private Pattern pattern = Pattern.compile("/shutdown/(?<token>.*?)");

    public ShutdownHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        String uri = request.getRequestLine().getUri();
        Matcher matcher = pattern.matcher(uri);
        if (matcher.matches()) {
            String token = matcher.group("token");
            String storedToken = Configs.getConfigs(StartLog.CONFIG_KEY, String.class, null);

            if (token.equals(storedToken)) {
                this.server.shutdown(5);
            } else {
                logger.info(String.format("someone tried to shutdown server with token : %s", token));
            }
        }
    }
}
