package orm.util;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SQLUtil {

    private static final Calendar CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));

    public static void setPSbyFieldAtIndex(PreparedStatement preparedStatement, int index, Object param, String typeName) throws SQLException {
        switch (typeName) {
            case "boolean":
            case "java.lang.Boolean":
                if (null == param) {
                    preparedStatement.setByte(index, (byte) 0);
                } else {
                    preparedStatement.setByte(index, (Boolean) param ? Byte.parseByte("1") : Byte.parseByte("0"));
                }
                break;
            case "byte":
            case "java.lang.Byte":
                if (null == param) {
                    preparedStatement.setByte(index, (byte) 0);
                } else {
                    preparedStatement.setByte(index, (Byte) param);
                }
                break;
            case "short":
            case "java.lang.Short":
                if (null == param) {
                    preparedStatement.setShort(index, (short) 0);
                } else {
                    preparedStatement.setShort(index, (Short) param);
                }
                break;
            case "int":
            case "java.lang.Integer":
                if (null == param) {
                    preparedStatement.setInt(index, 0);
                } else {
                    preparedStatement.setInt(index, (Integer) param);
                }
                break;
            case "long":
            case "java.lang.Long":
                if (null == param) {
                    preparedStatement.setLong(index, 0);
                } else {
                    preparedStatement.setLong(index, (Long) param);
                }
                break;
            case "float":
            case "java.lang.Float":
                if (null == param) {
                    preparedStatement.setFloat(index, 0f);
                } else {
                    preparedStatement.setFloat(index, (Float) param);
                }
                break;
            case "double":
            case "java.lang.Double":
                if (null == param) {
                    preparedStatement.setDouble(index, 0d);
                } else {
                    preparedStatement.setDouble(index, (Double) param);
                }
                break;
            case "java.math.BigDecimal":
                if (null == param) {
                    preparedStatement.setBigDecimal(index, BigDecimal.ZERO);
                } else {
                    preparedStatement.setBigDecimal(index, (BigDecimal) param);
                }
                break;
            case "java.util.Date":
                if (null == param) {
                    preparedStatement.setTimestamp(index, new Timestamp(0L), CALENDAR);
                } else {
                    preparedStatement.setTimestamp(index, new Timestamp(((Date) param).getTime()), CALENDAR);
                }
                break;
            case "java.lang.String":
                if (null == param) {
                    preparedStatement.setString(index, "");
                } else {
                    preparedStatement.setString(index, (String) param);
                }
                break;
            case "java.io.Reader":
                preparedStatement.setCharacterStream(index, (Reader) param);
                break;
            case "java.io.InputStream":
                preparedStatement.setBinaryStream(index, (InputStream) param);
                break;
            default:
                throw new RuntimeException("unknown type " + typeName);
        }
    }

    public static void fill(String typeName, Object one, Method setterMethod, ResultSet resultSet, boolean isIntParam, Integer index, String columnName) throws SQLException, InvocationTargetException, IllegalAccessException {
        switch (typeName) {
            case "boolean":
            case "java.lang.Boolean":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getByte(index) != 0);
                } else {
                    setterMethod.invoke(one, resultSet.getByte(columnName) != 0);
                }
                break;
            case "byte":
            case "java.lang.Byte":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getByte(index));
                } else {
                    setterMethod.invoke(one, resultSet.getByte(columnName));
                }
                break;
            case "short":
            case "java.lang.Short":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getShort(index));
                } else {
                    setterMethod.invoke(one, resultSet.getShort(columnName));
                }
                break;
            case "int":
            case "java.lang.Integer":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getInt(index));
                } else {
                    setterMethod.invoke(one, resultSet.getInt(columnName));
                }
                break;
            case "long":
            case "java.lang.Long":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getLong(index));
                } else {
                    setterMethod.invoke(one, resultSet.getLong(columnName));
                }
                break;
            case "float":
            case "java.lang.Float":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getFloat(index));
                } else {
                    setterMethod.invoke(one, resultSet.getFloat(columnName));
                }
                break;
            case "double":
            case "java.lang.Double":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getDouble(index));
                } else {
                    setterMethod.invoke(one, resultSet.getDouble(columnName));
                }
                break;
            case "java.math.BigDecimal":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getBigDecimal(index));
                } else {
                    setterMethod.invoke(one, resultSet.getBigDecimal(columnName));
                }
                break;
            case "java.util.Date":
                Timestamp timestamp;
                if (isIntParam) {
                    timestamp = resultSet.getTimestamp(index, CALENDAR);
                } else {
                    timestamp = resultSet.getTimestamp(columnName, CALENDAR);
                }
                if (null == timestamp) {
                    setterMethod.invoke(one, new Date(0L));
                } else {
                    setterMethod.invoke(one, new Date(timestamp.getTime()));
                }
                break;
            case "java.lang.String":
                String string;
                if (isIntParam) {
                    string = resultSet.getString(index);
                } else {
                    string = resultSet.getString(columnName);
                }

                if (null == string) {
                    setterMethod.invoke(one, "");
                } else {
                    setterMethod.invoke(one, string);
                }
                break;
            case "java.io.Reader":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getCharacterStream(index));
                } else {
                    setterMethod.invoke(one, resultSet.getCharacterStream(columnName));
                }
                break;
            case "java.io.InputStream":
                if (isIntParam) {
                    setterMethod.invoke(one, resultSet.getBinaryStream(index));
                } else {
                    setterMethod.invoke(one, resultSet.getBinaryStream(columnName));
                }
                break;
            default:
                System.out.println(typeName);
                break;
        }
    }

}
