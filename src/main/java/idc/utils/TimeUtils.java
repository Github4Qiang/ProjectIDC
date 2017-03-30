package idc.utils;

import java.util.Date;

/**
 * Created by sky on 2017/3/11.
 */
public class TimeUtils {

    public static Date sqlToDate(java.sql.Timestamp date) {
        return new Date(date.getTime());
    }

}
