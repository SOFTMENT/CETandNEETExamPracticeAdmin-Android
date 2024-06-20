package in.softment.exampracticeadmin.Util;

import java.util.Calendar;
import java.util.Date;

public class Constants {

    public static Date currentDate = getCurrentDate();


    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -1);
        return calendar.getTime();
    }

}
