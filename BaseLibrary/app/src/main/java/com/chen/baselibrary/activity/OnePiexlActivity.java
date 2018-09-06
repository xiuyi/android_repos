package com.chen.baselibrary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chen.baselibrary.R;

public class OnePiexlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置1像素
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        window.setBackgroundDrawableResource(android.R.drawable.screen_background_light_transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_one_piexl;
    }

    @Override
    protected void onWidgetClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Log.i("TAG","拦截返回按钮");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
