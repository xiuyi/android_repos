package com.chen.baselibrary.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author chen
 * @date 2018/9/10 下午4:34
 * email xiuyi.chen@erinspur.com
 * desc 小米手机工具类
 */

public class XiaoMiUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    /**
     * 判断是否是miui系统
     * @return
     */
    public static boolean isMiUi(){
        Properties prop= new Properties();
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
            return true;
        }
        return false;
    }
}
