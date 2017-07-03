package orm.handler.retO;

import java.lang.reflect.Method;
import java.util.List;

public class RemoveRet {

    private String sql;
    private boolean isSoft;

    public String getSql() {
        return sql;
    }

    public RemoveRet setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public boolean isSoft() {
        return isSoft;
    }

    public RemoveRet setSoft(boolean soft) {
        this.isSoft = soft;
        return this;
    }
}
