package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BashUtil {

    private static final Logger logger = LogManager.getLogger(BashUtil.class);

    public static List<String> run(String cmd, boolean needOutput) throws IOException {
        logger.info(cmd);
        Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd });

        if (!needOutput) {
            return null;
        }
        List<String> ret = new ArrayList<>();

        InputStream stdin = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        logger.info("<OUTPUT>");

        while ( (line = br.readLine()) != null) {
            logger.info(line);
            ret.add(line);
        }

        logger.info("</OUTPUT>");
        int exitVal = 0;
        try {
            exitVal = process.waitFor();
        } catch (InterruptedException e) {
            logger.catching(e);
        }
        logger.info("Process exitValue: " + exitVal);

        return ret;
    }

}
