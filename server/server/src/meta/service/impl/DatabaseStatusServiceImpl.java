package meta.service.impl;

import config.Configs;
import config.InitConfig;
import meta.service.IDatabaseStatusService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.FileUtil;

import java.io.*;

public class DatabaseStatusServiceImpl implements IDatabaseStatusService {

    private static final Logger logger = LogManager.getLogger(DatabaseStatusServiceImpl.class);

    @Override
    public File[] listCfgFiles() {
        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        File databaseConfigDir = new File(initConfig.getConfigRoot(), "dbcfgs");
        if (!databaseConfigDir.exists() || !databaseConfigDir.isDirectory()) {
            return null;
        }

        return databaseConfigDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
    }

    private File getActiveCfgFile() {
        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        File databaseConfigFile = new File(initConfig.getConfigRoot(), "dbcfgs/active/db.json");
        return databaseConfigFile;
    }

    @Override
    public Boolean cpCfg2Active(File cfg) {
        File activeCfgFile = this.getActiveCfgFile();
        try {
            if (!activeCfgFile.exists()) {
                activeCfgFile.mkdirs();
                activeCfgFile.createNewFile();
            }
            return IOUtils.copy(new FileReader(cfg), new FileWriter(activeCfgFile)) != 0;
        } catch (IOException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public Boolean cpCfg2Active(String cfg) {
        File activeCfgFile = this.getActiveCfgFile();
        try {
            if (!activeCfgFile.exists()) {
                activeCfgFile.mkdirs();
                activeCfgFile.createNewFile();
            }
            return IOUtils.copy(new StringReader(cfg), new FileWriter(activeCfgFile)) != 0;
        } catch (IOException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public File activeCfgFile() {
        File activeCfgFile = this.getActiveCfgFile();
        if (activeCfgFile.exists()) {
            return activeCfgFile;
        }
        return null;
    }

    @Override
    public Boolean testCfgConn(File cfg) {
//        FileUtil.getObjectFromFile(cfg, );
        return null;
    }
}
