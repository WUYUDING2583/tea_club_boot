package com.yuyi.tea.common.utils;

public class NumberUtil {

    /**
     * 获取digit位数的随机数字
     * @param digit
     * @return
     */
    public static String RandomNumber(int digit) {
        String result="";
        for(int i=0;i<digit;i++) {
            result+=((int)(Math.random()*(9-0+1)));
        }
        return result;
    }
}
