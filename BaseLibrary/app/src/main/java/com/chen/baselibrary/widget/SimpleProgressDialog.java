package com.chen.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chen.baselibrary.R;


/**
 * Created by xiuyi.chen on 2016-05-25.
 * 圆形进度条
 */
public class SimpleProgressDialog extends Dialog{
    /**
     * 构造方法
     * @param context
     * @param strMessage 显示的文字信息，如果null，将不显示
     */
    public SimpleProgressDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    private SimpleProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.dialog_progress_simple);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
        if (strMessage != null) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(strMessage);
        }else{
            tvMsg.setVisibility(View.GONE);
        }

    }
}
