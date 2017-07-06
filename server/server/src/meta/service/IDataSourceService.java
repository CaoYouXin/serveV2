package meta.service;

import config.DataSourceConfig;
import rest.Service;

import java.sql.SQLException;

public interface IDataSourceService extends Service {

    DataSourceConfig newDataSourceConfig(String schema) throws SQLException;

}
