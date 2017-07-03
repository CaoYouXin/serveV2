package orm.handler.retO;

import java.lang.reflect.Method;
import java.util.List;

public class SaveRet {

    private String sql;
    private boolean isInsert;
    private List<Method> getters;
    private Method idSetter;

    public String getSql() {
        return sql;
    }

    public SaveRet setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public boolean isInsert() {
        return isInsert;
    }

    public SaveRet setInsert(boolean insert) {
        isInsert = insert;
        return this;
    }

    public List<Method> getGetters() {
        return getters;
    }

    public SaveRet setGetters(List<Method> getters) {
        this.getters = getters;
        return this;
    }

    public Method getIdSetter() {
        return idSetter;
    }

    public SaveRet setIdSetter(Method idSetter) {
        this.idSetter = idSetter;
        return this;
    }
}
