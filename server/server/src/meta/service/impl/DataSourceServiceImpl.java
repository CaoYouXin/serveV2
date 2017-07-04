package meta.service.impl;

import config.Configs;
import config.DataSourceConfig;
import config.InitConfig;
import meta.service.IDataSourceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import rest.JsonResponse;
import rest.RestHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceServiceImpl implements IDataSourceService {

    private static final Logger logger = LogManager.getLogger(DataSourceServiceImpl.class);

    @Override
    public DataSourceConfig newDataSourceConfig(String schema) throws SQLException {
        InitConfig configs = Configs.getConfigs(InitConfig.CONFIG_KEY, InitConfig.class);
        DataSourceConfig dataSource = configs.getDataSource();

        DatasourceFactory.newDataSource(
                dataSource.getUrl(),
                dataSource.getUser(),
                dataSource.getPwd()
        );

        try (Connection conn = DatasourceFactory.getMySQLDataSource().getConnection()) {
            Statement statement = conn.createStatement();
            String sql = String.format("CREATE SCHEMA `%s` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin", schema);
            logger.info(sql);
            statement.execute(sql);
        }

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(dataSource.getUrl() + '/' + schema);
        dataSourceConfig.setUser(dataSource.getUser());
        dataSourceConfig.setPwd(dataSource.getPwd());

        DatasourceFactory.newDataSource(
                dataSourceConfig.getUrl(),
                dataSourceConfig.getUser(),
                dataSourceConfig.getPwd()
        );
        return dataSourceConfig;
    }
}
