package util;

public class BoolUtil {

    public static Boolean parseTF(String tf) {
        if ("T".equals(tf)) {
            return true;
        }

        if ("F".equals(tf)) {
            return false;
        }

        return null;
    }

}
