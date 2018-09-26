package com.chen.baselibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(this.sp);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        checkNotNull(this.sp);
        return this.sp.getString(key, defaultValue);
    }

    public void putBoolean(String key, Boolean value) {
        checkNotNull(this.sp);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        checkNotNull(this.sp);
        return this.sp.getBoolean(key, defaultValue);
    }

    public void putInt(String key,int value){
        checkNotNull(this.sp);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        checkNotNull(this.sp);
        return this.sp.getInt(key, defaultValue);
    }

    public void putFloat(String key,float value){
        checkNotNull(this.sp);
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key, float defaultValue) {
        checkNotNull(this.sp);
        return this.sp.getFloat(key, defaultValue);
    }
}
