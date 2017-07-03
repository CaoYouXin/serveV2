package util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class StringUtil {

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new RuntimeException("MD5加密出现错误");
        }
    }

    public static int indexOf(String str, int index, String... search) {
        int indexOf = -1;
        for (String c : search) {
            indexOf = str.indexOf(c, index);
            if (-1 != indexOf) {
                return indexOf;
            }
        }
        return indexOf;
    }

    public static int indexOf(String str, int index, char... search) {
        int indexOf = -1;
        for (char c : search) {
            indexOf = str.indexOf(c, index);
            if (-1 != indexOf) {
                return indexOf;
            }
        }
        return indexOf;
    }

    public static String cutPrefix(String str, String... prefix) {
        for (String s : prefix) {
            if (str.startsWith(s)) {
                return str.substring(s.length());
            }
        }
        return str;
    }

}
