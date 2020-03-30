package com.yuyi.tea.common.utils;

import java.util.Date;

public class TimeUtil {
    /**
     * @return 当前时间的时间戳
     */
    public static long getCurrentTimestamp(){
        return new Date().getTime();
    }
}
