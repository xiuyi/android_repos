package com.chen.baselibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.baselibrary.R;


/**
 * 显示TOAST的工具类
 * @author chenxiuyi
 *	复制需要layout:toast_icon.xml和toast_txt.xml两个布局文件
 *	复制需要drawable:toast_corner_bg.xml背景图
 */
public class ToastUtil {
	/**
	 * 定义显示位置的枚举类型
	 */
	public enum SHOW_POSITION {
		TOP(Gravity.TOP), CENTER(Gravity.CENTER_HORIZONTAL), BOTTOM(
				Gravity.BOTTOM);

		private int value = 0;

		private SHOW_POSITION(int value) { // 必须是private的，否则编译错误
			this.value = value;
		}

		public int value() {
			return this.value;
		}
	};

	private static Toast toast;

	
	/**
	 * 显示长时间的带图标的Toast
	 * 
	 * @param context
	 * @param strId 显示文字资源id
	 * @param icon 图标
	 * @param pos 位置
	 */
	public static void showLongIcon(Context context, int strId, int icon,
			SHOW_POSITION pos) {
		showIcon(context, getStringFromResourceId(context, strId), icon, pos, Toast.LENGTH_LONG);
	}
	/**
	 * 显示长时间的带图标的Toast
	 * 
	 * @param context
	 * @param msg 显示文字
	 * @param icon 图标
	 * @param pos 位置
	 */
	public static void showLongIcon(Context context,String msg, int icon,
			SHOW_POSITION pos) {
		showIcon(context, msg, icon, pos, Toast.LENGTH_LONG);
	}

	/**
	 * 显示短时间的带图标的Toast
	 * 
	 * @param context
	 * @param strId 显示文字资源ID
	 * @param icon 图标
	 * @param pos 位置
	 */
	public static void showShortIcon(Context context, int strId,
			int icon, SHOW_POSITION pos) {
		showIcon(context, getStringFromResourceId(context, strId), icon, pos, Toast.LENGTH_SHORT);
	}
	/**
	 * 显示短时间的带图标的Toast
	 * 
	 * @param context
	 * @param msg 显示文字
	 * @param icon 图标
	 * @param pos 位置
	 */
	public static void showShortIcon(Context context, String msg,
			int icon, SHOW_POSITION pos) {
		showIcon(context, msg, icon, pos, Toast.LENGTH_SHORT);
	}
	
	/**
	 * 显示长时间的不带图标的Toast
	 * 
	 * @param context
	 * @param msg 显示文字
	 * @param pos 位置
	 */
	public static void showLongTxt(Context context, String msg,
			SHOW_POSITION pos) {
		showTxt(context, msg, pos, Toast.LENGTH_LONG);
	}
	/**
	 * 显示长时间的不带图标的Toast
	 * 
	 * @param context
	 * @param strId 显示文字资源id
	 * @param pos 位置
	 */
	public static void showLongTxt(Context context, int strId,
			SHOW_POSITION pos) {
		showTxt(context, getStringFromResourceId(context, strId), pos, Toast.LENGTH_LONG);
	}

	/**
	 * 显示短时间的不带图标的Toast
	 * 
	 * @param context
	 * @param strId 显示文字
	 * @param pos 位置
	 */
	public static void showShortTxt(Context context, int strId, SHOW_POSITION pos) {
		showTxt(context, getStringFromResourceId(context, strId), pos, Toast.LENGTH_SHORT);
	}
	/**
	 * 显示短时间的不带图标的Toast
	 * 
	 * @param context
	 * @param msg 显示文字
	 * @param pos 位置
	 */
	public static void showShortTxt(Context context, String msg, SHOW_POSITION pos) {
		showTxt(context, msg, pos, Toast.LENGTH_SHORT);
	}
	
	/**
	 * 显示带图标的Toast
	 * 
	 * @param context
	 * @param tvString 显示内容
	 * @param icon 显示图标
	 * @param showPosition 显示位置
	 * @param duration 显示时长
	 */
	@SuppressLint("ResourceAsColor")
	private static void showIcon(Context context, String tvString, int icon,
			SHOW_POSITION showPosition, int duration) {
		View layout = LayoutInflater.from(context).inflate(R.layout.toast_icon,
				null);
		TextView text = (TextView) layout.findViewById(R.id.text);
		ImageView mImageView = (ImageView) layout.findViewById(R.id.iv_icon);
		mImageView.setImageResource(icon);
		text.setText(tvString);
		if(toast!=null){
			toast.cancel();
		}
		toast = new Toast(context);
		int yOffset = 0;
		int gravity = showPosition.value();
		switch (showPosition.value()) {
		case Gravity.TOP:
			yOffset = -20;
			break;
		case Gravity.BOTTOM:
			yOffset = 20;
			break;
			default:
		}
		toast.setGravity(gravity, 0, yOffset);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}
	/**
	 * 显示不带图标的Toast
	 * 
	 * @param context
	 * @param tvString 显示内容
	 * @param showPosition 显示位置
	 * @param duration 显示时长
	 */
	@SuppressLint("ResourceAsColor")
	private static void showTxt(Context context, String tvString,
			SHOW_POSITION showPosition, int duration) {
		View layout = LayoutInflater.from(context).inflate(R.layout.toast_txt,
				null);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(tvString);
		if(toast!=null){
			toast.cancel();
		}
		toast = new Toast(context);
		int yOffset = 0;
		int gravity = showPosition.value();
		switch (showPosition.value()) {
		case Gravity.TOP:
			yOffset = -20;
			break;
		case Gravity.BOTTOM:
			yOffset = 20;
			break;
			default:
		}
		toast.setGravity(gravity, 0, yOffset);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}
	
	/**
	 * 显示网络错误的提示
	 */
	public static void showNetErrorToast(Context context){
		showShortIcon(context, R.string.net_error, R.drawable.neterror_white_96px, SHOW_POSITION.CENTER);
	}
	/**
	 * 从资源ID获取文字
	 */
	protected static String getStringFromResourceId(Context context,int id){
		String str = context.getResources().getString(id);
		return str==null?"":str;
	}
}
