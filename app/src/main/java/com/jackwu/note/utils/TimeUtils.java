package com.jackwu.note.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JackWu on 2017/9/3.
 */

public class TimeUtils {
    /**
     *把时间转化成2016年05月20日 11:11
     * */
    public static String getSimpleDateFormat(Date time) {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA).format(time);
    }
    // public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);//把时间转化成2016年05月20日 11:11
}
