package com.yuyi.tea.common.utils;

import com.sun.jmx.snmp.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static long ONE_DAY_TOTAL_MILLION_SECONDS=1000*60*60*24;
    /**
     * @return 当前时间的时间戳
     */
    public static long getCurrentTimestamp(){
        return new Date().getTime();
    }

    /**
     * 获取N天前的零点时间戳
     * @param N
     * @return
     */
    public static long getNDayAgoStartTime(int N) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime()-N*ONE_DAY_TOTAL_MILLION_SECONDS;
    }

    /**
     * 将时间戳转变为yyyy-MM-dd格式时间字符串
     * @param timestamp
     * @return
     */
    public static String convertTimestampToTimeFormat(long timestamp){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);
        return tsStr;
    }

}
