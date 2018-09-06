package com.chen.baselibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chen.baselibrary.R;
import com.chen.baselibrary.utils.DimentUtils;


/**
 * Created by xiuyi.chen on 2016-05-25.
 * 多按钮的Dialog
 */
public class ButtonDialog extends Dialog{
    /**
     * 构造方法
     * @param context
     * @param strMessage 显示的文字信息,根据顺序显示
     */
    public ButtonDialog(Context context, String[] strMessage,OnItemClickListener itemClickListener) {
        this(context, R.style.CustomButtonDialog, strMessage,itemClickListener);
    }

    private ButtonDialog(Context context, int theme, String[] strMessage, final OnItemClickListener itemClickListener) {
        super(context, theme);
        this.setContentView(R.layout.dialog_button);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        LinearLayout wrapper = (LinearLayout) this.findViewById(R.id.wrapper);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                itemClickListener.onItemClick(ButtonDialog.this,v,tag);
            }
        };
        wrapper.removeAllViews();
        for (int i=0;i<strMessage.length;i++){
            TextView btn = new TextView(context);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            btn.setBackgroundResource(R.drawable.selector_dialog_button_item);
            int padding10 = (int) DimentUtils.dipToPx(context,10);
            int padding20 = (int) DimentUtils.dipToPx(context,20);
            btn.setPadding(padding20,padding10,padding20,padding10);
            btn.setTextSize(18);
            btn.setText(strMessage[i]);
            btn.setTextColor(Color.BLACK);
            btn.setTag(i);
            btn.setOnClickListener(clickListener);
            wrapper.addView(btn);
        }
    }

    /**
     * 选项被点击的回调接口
     */
    public interface OnItemClickListener{
        /**
         * 回调方法
         * @param view
         * @param index view的位置索引，与strMessage的索引一致从0开始
         */
        void onItemClick(Dialog dialog, View view, int index);
    }
}
