package server;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.SocketTimeoutException;

public class StdErrorExceptionLogger implements ExceptionLogger {

    private static Logger logger = LogManager.getLogger(StdErrorExceptionLogger.class);

    @Override
    public void log(Exception ex) {
        if (ex instanceof SocketTimeoutException) {
            logger.error("Connection timed out");
        } else if (ex instanceof ConnectionClosedException) {
            logger.error(ex.getMessage());
        } else {
            logger.catching(ex);
        }
    }

}
