package com.yuyi.tea.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断字符串是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str){
        Pattern compile = Pattern.compile("[0-9]+");
        Matcher isNum = compile.matcher(str);
        if(isNum.matches()){
            return true;
        }
        return false;
    }
}
