package com.chen.baselibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chen.baselibrary.R;


/**
 * 带有ToolBar的基类Activity，继承自BaseActivity
 * 1、显示ProgressDialog
 * 2、显示自定义Toast
 * 3、保存屏幕宽高
 * 4、使用注解代替findViewById
 * 5、携带ToolBar，布局文件中需配置id为title_bar的android.support.v7.widget.Toolbar
 * 如：
 * <android.support.v7.widget.Toolbar
 *  android:id="@+id/title_bar"
 *  android:layout_width="match_parent"
 *  android:layout_height="wrap_content"/>
 */
public abstract class BaseToolBarActivity extends BaseActivity{
    protected Toolbar toolbar;
    protected TextView titleView;
    protected ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = this.findViewById(R.id.title_bar);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        for(int i=0;i<toolbar.getChildCount();i++){
            View itemView = toolbar.getChildAt(i);
            if(itemView instanceof  TextView){
                titleView = (TextView) itemView;
                break;
            }
        }
        //子类可以直接使用该actionBar
        //是否显示标题
        //actionBar.setDisplayShowTitleEnabled(true);
        //是否显示返回按钮
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("新建联系人");
    }

}
