package com.chen.baselibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类
 * Created by chen on 2018/5/4.
 */

public class SharePreferenceUtils {
    private static final SharePreferenceUtils instance = new SharePreferenceUtils();
    private SharedPreferences sp;

    private SharePreferenceUtils() {
    }

    public static SharePreferenceUtils getInstance() {
        return instance;
    }

    /**
     * 必须首先调用init方法
     *
     * @param context
     * @param fileName
     */
    public void init(Context context, String fileName) {
        this.sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return this.sp.getString(key, defaultValue);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return this.sp.getBoolean(key, defaultValue);
    }

    public void putInt(String key,int value){
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.sp.getInt(key, defaultValue);
    }

    public void putFloat(String key,float value){
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return this.sp.getFloat(key, defaultValue);
    }
}
