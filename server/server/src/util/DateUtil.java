package util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date calc(Date origin, int timeUnit, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(origin);
        c.add(timeUnit, amount);
        return c.getTime();
    }

}
