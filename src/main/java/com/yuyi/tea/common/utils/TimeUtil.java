package com.yuyi.tea.common.utils;

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

}
