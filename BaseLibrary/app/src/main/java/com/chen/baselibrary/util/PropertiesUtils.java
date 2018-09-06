package com.chen.baselibrary.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chen on 2017/4/14.
 * 属性文件读取工具
 */

public class PropertiesUtils {
    /**
     * 读取属性文件的key值
     * @param context
     * @param name
     * @return
     */
    public static String getProperty(Context context, String name){
        String property = null;
        try {
            InputStream is = context.getAssets().open("config.properties");
            Properties p = new Properties();
            p.load(is);
            if(p.containsKey(name)){
                property = p.getProperty(name);
                is.close();
                return property;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }
}
