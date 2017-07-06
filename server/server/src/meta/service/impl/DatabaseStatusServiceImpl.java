package meta.service.impl;

import beans.BeanManager;
import config.Configs;
import config.DataSourceConfig;
import config.InitConfig;
import meta.repository.ITestRepo;
import meta.service.IDatabaseStatusService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import orm.RepositoryManager;
import util.FileUtil;

import java.io.*;

public class DatabaseStatusServiceImpl implements IDatabaseStatusService {

    private static final Logger logger = LogManager.getLogger(DatabaseStatusServiceImpl.class);

    private static final ITestRepo TEST_REPO = BeanManager.getInstance().getRepository(ITestRepo.class);

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
                activeCfgFile.getParentFile().mkdirs();
                activeCfgFile.createNewFile();
            }
            FileReader input = new FileReader(cfg);
            FileWriter output = new FileWriter(activeCfgFile);
            boolean suc = IOUtils.copy(input, output) != 0;
            input.close();
            output.flush();
            output.close();

            return suc;
        } catch (IOException e) {
            logger.catching(e);
            return false;
        }
    }

    @Override
    public Boolean cpCfg2Active(String cfg, String backup) {
        InitConfig initConfig = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        File backupFile = new File(initConfig.getConfigRoot(), "dbcfgs/" + backup + ".json");

        File activeCfgFile = this.getActiveCfgFile();
        try {
            if (!activeCfgFile.exists()) {
                activeCfgFile.getParentFile().mkdirs();
                activeCfgFile.createNewFile();
            }
            if (!backupFile.exists()) {
                backupFile.getParentFile().mkdirs();
                backupFile.createNewFile();
            }
            StringReader input1 = new StringReader(cfg);
            FileWriter output1 = new FileWriter(activeCfgFile);
            StringReader input2 = new StringReader(cfg);
            FileWriter output2 = new FileWriter(backupFile);

            boolean suc = (IOUtils.copy(input1, output1) != 0) && (IOUtils.copy(input2, output2) != 0);
            input1.close();
            input2.close();
            output1.flush();
            output1.close();
            output2.flush();
            output2.close();

            return suc;
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
        DataSourceConfig dataSourceConfig = FileUtil.getObjectFromFile(cfg, DataSourceConfig.class);

        DatasourceFactory.newDataSource(
                dataSourceConfig.getUrl(),
                dataSourceConfig.getUser(),
                dataSourceConfig.getPwd()
        );

        return TEST_REPO.createTableIfNotExist();
    }
}
