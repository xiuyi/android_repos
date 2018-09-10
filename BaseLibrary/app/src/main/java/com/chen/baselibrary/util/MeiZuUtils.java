package com.chen.baselibrary.util;

import java.lang.reflect.Method;

/**
 * @author chen
 * @date 2018/9/10 下午4:31
 * email xiuyi.chen@erinspur.com
 * desc 魅族手机工具类
 */

public class MeiZuUtils {
    /**
     * 判断是否是flyme系统
     * @return
     */
    public static boolean isFlyme(){
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            String flymeFlag = (String)get.invoke(clz, "ro.build.display.id", "");
            return flymeFlag.toLowerCase().contains("flyme");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
