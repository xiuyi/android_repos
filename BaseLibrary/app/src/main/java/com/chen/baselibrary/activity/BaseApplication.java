package com.chen.baselibrary.activity;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.tencent.bugly.Bugly;

/**
 * @author chen
 * @date 2018/9/7 下午1:46
 * email xiuyi.chen@erinspur.com
 * desc Application基类
 */
public class BaseApplication extends Application{
    protected static BaseApplication instance;
    //屏幕宽高
    protected int SCREEN_WIDTH,SCREEN_HEIGHT;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //bugly以及应用升级SDK初始化
        Bugly.init(getApplicationContext(), "22123491d4", false);
        initScreenSize();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    /**
     * 初始化屏幕尺寸
     */
    private void initScreenSize(){
        //初始化 屏幕尺寸参数
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //float density1 = dm.density;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }
    /**
     * 获取屏幕宽度
     */
    public int getScreenWidth(){
        return this.SCREEN_WIDTH;
    }

    /**
     * 获取屏幕高度
     */
    public int getScreenHeight(){
        return this.SCREEN_HEIGHT;
    }
}
