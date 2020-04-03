package com.yuyi.tea.common.utils;

import java.util.UUID;

public class TokenUtil {

    //生成token
    public static String getToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
