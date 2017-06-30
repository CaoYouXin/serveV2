package beans;

import org.apache.logging.log4j.LogManager;

public interface ChangeOriginI {

    default void changeOrigin(Object obj) {
        LogManager.getLogger(ChangeOriginI.class).info("change origin : " + obj);
    }

}
