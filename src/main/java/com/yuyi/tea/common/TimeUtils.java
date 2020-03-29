package com.yuyi.tea.common;

import java.util.Date;

public class TimeUtils {
    /**
     * @return 当前时间的时间戳
     */
    public static long getCurrentTimestamp(){
        return new Date().getTime();
    }
}
