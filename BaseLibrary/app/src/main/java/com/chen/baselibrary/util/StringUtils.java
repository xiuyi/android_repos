package com.chen.baselibrary.util;

/**
 * Created by chen on 2017/4/25.
 */

public class StringUtils {
    /**
     * 判断str是否是null或者长度为0
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return str==null||str.length()==0;
    }
}
