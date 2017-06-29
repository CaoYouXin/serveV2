package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Configs;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    private static Logger logger = LogManager.getLogger(FileUtil.class);

    public static String getContent(File file) {

        String result = "";

        try {
            result = IOUtils.toString(new FileInputStream(file));
        } catch (IOException e) {
            logger.catching(e);
        }

        return result;
    }

    public static <T> T getObjectFromFile(File file, Class<T> clazz) {
        String content = getContent(file);
        try {
            return Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class).readValue(content, clazz);
        } catch (IOException e) {
            logger.catching(e);
            return null;
        }
    }

    public static void writeObjectToFile(Object object, File file) {
        try {
            IOUtils.write(Configs.getConfigs(Configs.OBJECT_MAPPER, ObjectMapper.class).writeValueAsString(object),
                    new FileOutputStream(file), Consts.UTF_8);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

}
