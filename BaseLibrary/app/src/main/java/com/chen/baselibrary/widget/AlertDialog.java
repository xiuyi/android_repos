package com.chen.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chen.baselibrary.R;


/**
 * Created by xiuyi.chen on 2016-05-25.
 * 带有【确定】、【取消】按钮的提示dialog
 */
public class AlertDialog extends Dialog{
    /**
     * 构造方法
     * @param context
     * @param strMessage 显示的文字信息
     */
    public AlertDialog(Context context, String strMessage, OnButtonClickListener buttonClickListener) {
        this(context, R.style.MyAlertDialog, strMessage,buttonClickListener);
    }

    private AlertDialog(Context context, int theme, String strMessage, final OnButtonClickListener buttonClickListener) {
        super(context, theme);
        this.setContentView(R.layout.dialog_alert);
        this.setCanceledOnTouchOutside(false);
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView positive = (TextView) findViewById(R.id.tv_positive);
        TextView cancle = (TextView) findViewById(R.id.tv_cancle);
        title.setText(strMessage);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tv_positive){
                    buttonClickListener.onPositiveClick(v);
                }else if(v.getId() == R.id.tv_cancle){
                    buttonClickListener.onCancleClick(v);
                }
                //点击按钮后自动关闭
                dismiss();
            }
        };
        positive.setOnClickListener(listener);
        cancle.setOnClickListener(listener);
    }

    /**
     * 确定、取消按钮被点击的回调
     */
    public interface OnButtonClickListener{
        /**
         * 确定按钮被点击
         * @param view
         */
        void onPositiveClick(View view);
        /**
         * 取消按钮被点击
         */
        void onCancleClick(View view);
    }
}
