package com.yuyi.tea.common.utils;

import org.springframework.util.StringUtils;

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

    public static String base64Process(String base64Str) {
        if (!StringUtils.isEmpty(base64Str)) {
            String photoBase64 = base64Str.substring(0, 30).toLowerCase();
            int indexOf = photoBase64.indexOf("base64,");
            if (indexOf > 0) {
                base64Str = base64Str.substring(indexOf + 7);
            }

            return base64Str;
        } else {
            return "";
        }
    }
}
