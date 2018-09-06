package com.chen.baselibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 针对华为手机的工具类
 * Created by chen on 2018/8/30.
 * @author chen
 */
public class HuaWeiUtils {
    /**
     * 判断是否是华为手机
     * @return
     */
    public static boolean isHuaWeiPhone(Context context){
        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        int hwid = 0;
        try {
            pi = pm.getPackageInfo("com.huawei.hwid", 0);
            if (pi != null) {
                int  result = pi.versionCode;
                Log.i("hwid",""+result);
                return result > 0;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否是Emui系统
     * @return
     */
    public static boolean isEmui(){
        return getEmuiVersion() > 0;
    }

    /**
     * 获取EMUI系统版本4.1的版本号是9  >13就是8.0系统
     * @return
     */
    public static int getEmuiVersion(){
        int emuiApiLevel = 0;
        try {
            Class cls =  Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new  Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new  Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emuiApiLevel;
    }
}
