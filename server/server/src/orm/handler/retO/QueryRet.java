package orm.handler.retO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryRet {

    private String select;
    private String from;
    private String where;
    private String groupBy;
    private String having;
    private String orderBy;
    private Map<String, Method> column2setter;
    private List<String> retColumns;
    private Map<String, Class<?>> alias2type;
    private List<Integer> params;

    public String getSql() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (where != null) {
            stringBuilder.append(" Where ").append(where);
        }

        if (groupBy != null) {
            stringBuilder.append(" Group By ").append(groupBy);
            if (having != null) {
                stringBuilder.append(" Having ").append(having);
            }
        }

        if (orderBy != null) {
            stringBuilder.append(" Order By ").append(orderBy);
        }

        return String.format("Select %s From %s %s", select, from, stringBuilder.toString());
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Map<String, Method> getColumn2setter() {
        return column2setter;
    }

    public void setColumn2setter(Map<String, Method> column2setter) {
        this.column2setter = column2setter;
    }

    public List<String> getRetColumns() {
        return retColumns;
    }

    public void setRetColumns(List<String> retColumns) {
        this.retColumns = retColumns;
    }

    public Map<String, Class<?>> getAlias2type() {
        return alias2type;
    }

    public void setAlias2type(Map<String, Class<?>> alias2type) {
        if (null == this.alias2type) {
            this.alias2type = alias2type;
        } else {
            this.alias2type.putAll(alias2type);
        }
    }

    public String getAllAlias() {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String key : this.alias2type.keySet()) {
            stringJoiner.add(key);
        }
        return stringJoiner.toString();
    }

    public List<Integer> getParams() {
        return params;
    }

    public void setParams(Integer params) {
        if (null == this.params) {
            this.params = new ArrayList<>();
        }

        this.params.add(params);
    }

    public void transformFrom(Map<String, String> queryMd5) {
        if (queryMd5.size() > 0) {
            StringJoiner replaceRegex = new StringJoiner("|", "(?<md5>", ")");
            for (String md5 : queryMd5.keySet()) {
                replaceRegex.add(md5);
            }
            Pattern p1 = Pattern.compile(replaceRegex.toString());
            Matcher m1 = p1.matcher(this.from);
            StringBuffer sb1 = new StringBuffer();
            while (m1.find()) {
                String md5 = m1.group("md5");

                m1.appendReplacement(sb1, queryMd5.get(md5));
            }
            m1.appendTail(sb1);

            this.from = sb1.toString();
        }
    }
}
