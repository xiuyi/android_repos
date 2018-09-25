package com.chen.baselibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.chen.baselibrary.activity.BaseApplication;

/**
 * @author chen
 * @date 2018/9/10 下午4:27
 * email xiuyi.chen@erinspur.com
 * desc 系统工具类
 */

public class SystemUtils {

    public static final String SYS_EMUI = "emui";
    public static final String SYS_MIUI = "miui";
    public static final String SYS_FLYME = "flyme";
    // 默认 未知
    public static final String SYS_UNKNOW = "";

    /**
     * 获取系统的名称，能够判断小米、华为、魅族系统
     *
     * @return 系统名称
     */
    public static String getSystemName() {
        String SYS = SYS_UNKNOW;

        if (XiaoMiUtils.isMiUi()) {
            //小米
            SYS = SYS_MIUI;
        } else if (HuaWeiUtils.isEmui()) {
            //华为
            SYS = SYS_EMUI;
        } else if (MeiZuUtils.isFlyme()) {
            //魅族
            SYS = SYS_FLYME;
        }

        return SYS;
    }

    /**
     * 是否是debug模式
     *
     * @return
     */
    public static boolean isDebug() {
        try {
            ApplicationInfo info = BaseApplication.getInstance().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取APPid
     *
     * @return
     */
    public static String getAppId() {
        return BaseApplication.getInstance().getApplicationInfo().processName;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName() {
        try {
            Context context = BaseApplication.getInstance();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用程序版本名称信息
     *
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode() {
        try {
            Context context = BaseApplication.getInstance();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取应用程序包名
     *
     * @return 当前应用的包名
     */
    public static synchronized String getPackageName() {
        try {
            Context context = BaseApplication.getInstance();
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     */
    public static synchronized Bitmap getBitmap() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        Context context = BaseApplication.getInstance();
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo);
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

}
