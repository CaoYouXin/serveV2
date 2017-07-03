package orm;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DatasourceFactory {

    private static final Logger logger = LogManager.getLogger(DatasourceFactory.class);

    private static DataSource DATA_SOURCE;

//    static {
//        newDataSource(
//                "jdbc:mysql://localhost:3306",
//                "root", "root"
//        );
//    }

    public static DataSource getMySQLDataSource() {
        return DATA_SOURCE;
    }

    public static void newDataSource(String url, String user, String pwd) {
        MysqlDataSource mysqlDS = new MysqlDataSource();
        mysqlDS.setURL(url);
        mysqlDS.setUser(user);
        mysqlDS.setPassword(pwd);
        try {
            mysqlDS.setLoginTimeout(5);
            mysqlDS.setResultSetSizeThreshold(1000);
        } catch (SQLException e) {
            logger.catching(e);
        }
        DATA_SOURCE = mysqlDS;
    }

}
