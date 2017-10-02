package orm.handler;

import beans.BeanManager;
import beans.EntityBeanI;
import config.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import orm.DatasourceFactory;
import orm.Query;
import orm.handler.retO.QueryRet;
import orm.util.SQLUtil;
import util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryHandler implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(QueryHandler.class);
    private static final String PATTERN = "Select\\s+(?<select>.+?)\\s+From\\s+(?<from>.+?)(?:\\s+Where\\s+(?<where>.+?))*(?:\\s+Group By\\s+(?<groupBy>.+?)(?:\\s+Having\\s+(?<having>.+?))*)*(?:\\s+Order By\\s+(?<orderBy>.+?))*";
    private static final Pattern SELECT_PATTERN = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
    private static final Pattern CHILD_SELECT_PATTERN = Pattern.compile("\\((?<query>" + PATTERN + ")\\)\\s+(?<alias>.+?)(?<after>,|\\s)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PARAM_PATTERN = Pattern.compile(":(?<idx>\\d+)");

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query query = method.getDeclaredAnnotation(Query.class);
        if (null == query) {
            throw new RuntimeException("it has query just now.");
        }

        Map<String, Class<?>> stringClassMap = new HashMap<>();
        for (Class<?> type : query.types()) {
            String typeName = type.getTypeName();
            int lastIndexOf = typeName.lastIndexOf('.');
            if (-1 == lastIndexOf) {
                stringClassMap.put(typeName, type);
            } else {
                stringClassMap.put(typeName.substring(lastIndexOf + 1), type);
            }
        }

        boolean retList = false;
        Class<?> retType = null;
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType.getTypeName().startsWith("java.util.List")) {
            retList = true;
            String typeName = genericReturnType.getTypeName();
            typeName = typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>'));
            ClassLoader classLoader = Configs.getConfigs(Configs.CLASSLOADER, ClassLoader.class,
                    () -> getClass().getClassLoader());
            retType = classLoader.loadClass(typeName);

            int lastIndexOf = typeName.lastIndexOf('.');
            if (-1 == lastIndexOf) {
                stringClassMap.put(typeName, retType);
            } else {
                stringClassMap.put(typeName.substring(lastIndexOf + 1), retType);
            }
        } else {
            retType = method.getReturnType();
            String typeName = retType.getTypeName();
            int lastIndexOf = typeName.lastIndexOf('.');
            if (-1 == lastIndexOf) {
                stringClassMap.put(typeName, retType);
            } else {
                stringClassMap.put(typeName.substring(lastIndexOf + 1), retType);
            }
        }

        QueryRet parsedSQL = this.parseSQL(query.useValue() ? query.value() : (String) args[0], stringClassMap);
        if (null == parsedSQL) {
            throw new RuntimeException("parse sql error.");
        }

        Map<String, Method> column2setter = new HashMap<>();
        for (Method getterMethod : retType.getMethods()) {
            Column column = getterMethod.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            String columnName = StringUtil.cutPrefix(getterMethod.getName(), "get", "is");
            String setterMethodName = "set" + columnName;
            Method setterMethod = retType.getMethod(setterMethodName, getterMethod.getReturnType());
            column2setter.put(columnName, setterMethod);
        }
        parsedSQL.setColumn2setter(column2setter);

        Matcher matcher = PARAM_PATTERN.matcher(parsedSQL.getSql());
        while (matcher.find()) {
            String idx = matcher.group("idx");
            parsedSQL.setParams(Integer.parseInt(idx));
        }
        String finalSql = matcher.replaceAll("?");

        Connection conn = null;
        try {
            conn = DatasourceFactory.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(finalSql);
            logger.info(finalSql);

            if (null != parsedSQL.getParams()) {
                int i = 1;
                for (Integer idx : parsedSQL.getParams()) {
                    if (!query.useValue()) {
                        idx += 1;
                    }

                    SQLUtil.setPSbyFieldAtIndex(
                            preparedStatement, i++, args[idx], args[idx].getClass().getTypeName()
                    );
                }
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (retList) {
                List ret = new ArrayList();
                while (resultSet.next()) {
                    ret.add(getObject((Class<? extends EntityBeanI>) retType, parsedSQL, resultSet));
                }
                return ret;
            } else {
                if (!resultSet.next()) {
                    return null;
                }

                return getObject((Class<? extends EntityBeanI>) retType, parsedSQL, resultSet);
            }
        } finally {
            DatasourceFactory.closeConnection(conn);
        }
    }

    private Object getObject(Class<? extends EntityBeanI> type, QueryRet parsedSQL, ResultSet resultSet) throws SQLException, InvocationTargetException, IllegalAccessException {
        Object ret = BeanManager.getInstance().createBean(type);
        Map<String, Method> column2setter = parsedSQL.getColumn2setter();

        for (String column : parsedSQL.getRetColumns()) {
            Method setterMethod = column2setter.get(column);
            SQLUtil.fill(
                    setterMethod.getParameterTypes()[0].getTypeName(),
                    ret, setterMethod, resultSet,
                    false, null, column
            );
        }
        return ret;
    }

    private QueryRet parseSQL(String sqlCmd, Map<String, Class<?>> stringClassMap) {
        QueryRet queryRet = new QueryRet();

        Map<String, String> queryMd5 = new HashMap<>();
        String transformedSql = this.parseChildQuery(sqlCmd, queryRet, queryMd5, stringClassMap);

        Matcher matcher = SELECT_PATTERN.matcher(transformedSql);

        if (!matcher.matches()) {
            throw new RuntimeException("sql syntax error.");
        }

        String select = matcher.group("select");
        String from = matcher.group("from");
        String where = matcher.group("where");
        String groupBy = matcher.group("groupBy");
        String having = matcher.group("having");
        String orderBy = matcher.group("orderBy");

        this.parseFrom(from, queryRet, stringClassMap);
        queryRet.transformFrom(queryMd5);

        this.parseSelect(select, queryRet);

        if (null != where) {
            queryRet.setWhere(this.parseAlias(where, queryRet));
        }
        if (null != groupBy) {
            queryRet.setGroupBy(this.parseAlias(groupBy, queryRet));
        }
        if (null != having) {
            queryRet.setHaving(this.parseAlias(having, queryRet));
        }
        if (null != orderBy) {
            queryRet.setOrderBy(this.parseAlias(orderBy, queryRet));
        }

        return queryRet;
    }

    private String parseChildQuery(String sqlCmd, QueryRet queryRet, Map<String, String> queryMd5, Map<String, Class<?>> stringClassMap) {
        Map<String, Class<?>> alias2type = new HashMap<>();

        Matcher m0 = CHILD_SELECT_PATTERN.matcher(sqlCmd);
        StringBuffer sb0 = new StringBuffer();

        while (m0.find()) {
            String query = m0.group("query");
            String alias = m0.group("alias");
            String after = m0.group("after");

            alias2type.put(alias, String.class);

            String md5 = StringUtil.getMD5(query);
            queryMd5.put(md5, parseSQL(query, stringClassMap).getSql());
            m0.appendReplacement(sb0, String.format("(%s) %s%s", md5, alias, after));
        }
        m0.appendTail(sb0);
        queryRet.setAlias2type(alias2type);

        return sb0.toString();
    }

    private String parseAlias(String clause, QueryRet queryRet) {
        String regex = String.format("(?<alias>%s)\\.(?<field>\\S+?)(?<after>,|>|<|\\s|\\)|=)", queryRet.getAllAlias());
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(clause + " ");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String alias = m.group("alias");
            String field = m.group("field");
            String after = m.group("after");

            Class<?> type = queryRet.getAlias2type().get(alias);

            m.appendReplacement(sb, String.format("%s.`%s`%s", alias, this.getColumnDef(type, field), after));
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private void parseSelect(String select, QueryRet queryRet) {
        List<String> retColumns = new ArrayList<>();
        StringJoiner stringJoiner = new StringJoiner(",");

        for (String def : select.split(",")) {
            def = def.trim();
            if (def.contains("(")) {
                String[] defAndAlias = def.split("\\s+");

                if (defAndAlias.length != 2) {
                    throw new RuntimeException("sql syntax error.");
                }

                retColumns.add(defAndAlias[1]);

                String prefix = defAndAlias[0].substring(0, defAndAlias[0].indexOf('('));
                String suffix = defAndAlias[0].substring(defAndAlias[0].indexOf(')') + 1);

                def = defAndAlias[0].substring(defAndAlias[0].indexOf('(') + 1, defAndAlias[0].indexOf(')')).trim();

                if ("*".equals(def)) {
                    stringJoiner.add(String.format("%s(*)%s as `%s`", prefix, suffix, defAndAlias[1]));

                    continue;
                }

                String[] tableAndColumn = def.split("\\.");

                Class<?> type = queryRet.getAlias2type().get(tableAndColumn[0]);

                stringJoiner.add(String.format("%s(%s.`%s`)%s as `%s`", prefix, tableAndColumn[0], this.getColumnDef(type, tableAndColumn[1]), suffix, defAndAlias[1]));

            } else if (def.contains(".")) {

                String[] defAndAlias = def.split("\\s+");
                String[] tableAndColumn = defAndAlias[0].split("\\.");

                Class<?> type = queryRet.getAlias2type().get(tableAndColumn[0]);
                if (defAndAlias.length == 2) {
                    retColumns.add(defAndAlias[1]);

                    stringJoiner.add(String.format("%s.`%s` as `%s`", tableAndColumn[0], this.getColumnDef(type, tableAndColumn[1]), defAndAlias[1]));
                } else {
                    retColumns.add(tableAndColumn[1]);

                    stringJoiner.add(String.format("%s.`%s` as `%s`", tableAndColumn[0], this.getColumnDef(type, tableAndColumn[1]), tableAndColumn[1]));
                }
            } else {

                Class<?> type = queryRet.getAlias2type().get(def);

                for (Method method : type.getMethods()) {
                    Column column = method.getDeclaredAnnotation(Column.class);
                    if (null == column) {
                        continue;
                    }

                    String retColumn = StringUtil.cutPrefix(method.getName(), "get", "is");
                    retColumns.add(retColumn);
                    stringJoiner.add(String.format("%s.`%s` as `%s`", def, column.name(), retColumn));
                }
            }
        }

        queryRet.setRetColumns(retColumns);
        queryRet.setSelect(stringJoiner.toString());
    }

    private String getColumnDef(Class<?> type, String columnDef) {
        if (type.equals(String.class)) {
            return columnDef;
        }

        for (Method method : type.getMethods()) {
            Column column = method.getDeclaredAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            if (StringUtil.cutPrefix(method.getName(), "get", "is").equals(columnDef)) {
                return column.name();
            }
        }
        throw new RuntimeException("do not has a column : " + type.getTypeName() + ":" + columnDef + ".");
    }

    private void parseFrom(String from, QueryRet queryRet, Map<String, Class<?>> stringClassMap) {
        Map<String, Class<?>> alias2type = new HashMap<>();

        String regex = String.format("(?<typeName>%s) (?<alias>.+?)(?<rest> |,)", this.getAllTypeNames(stringClassMap));
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(from + " ");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String typeName = m.group("typeName");
            String alias = m.group("alias");
            String rest = m.group("rest");

            Class<?> type = stringClassMap.get(typeName);

            if (null == type) {
                throw new RuntimeException("select from not a entity");
            }

            Entity entity = type.getDeclaredAnnotation(Entity.class);
            if (null == entity) {
                throw new RuntimeException("select from not a entity");
            }

            alias2type.put(alias, type);
            m.appendReplacement(sb, String.format("`%s` %s%s", entity.name(), alias, rest));
        }
        m.appendTail(sb);

        queryRet.setAlias2type(alias2type);

        String transformedFrom = this.parseAlias(sb.toString(), queryRet);

        queryRet.setFrom(transformedFrom);
    }

    private String getAllTypeNames(Map<String, Class<?>> stringClassMap) {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String key : stringClassMap.keySet()) {
            stringJoiner.add(key);
        }
        return stringJoiner.toString();
    }


}
