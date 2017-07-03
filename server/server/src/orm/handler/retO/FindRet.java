package orm.handler.retO;

import java.lang.reflect.Method;
import java.util.Map;

public class FindRet {

    private String sql;
    private Map<String, Method> column2setter;

    public String getSql() {
        return sql;
    }

    public FindRet setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public Map<String, Method> getColumn2setter() {
        return column2setter;
    }

    public FindRet setColumn2setter(Map<String, Method> column2setter) {
        this.column2setter = column2setter;
        return this;
    }
}
