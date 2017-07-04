package meta.service;

import config.DataSourceConfig;

import java.sql.SQLException;

public interface IDataSourceService {

    DataSourceConfig newDataSourceConfig(String schema) throws SQLException;

}
