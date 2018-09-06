package com.chen.baselibrary.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 尺寸计算的工具类
 * @author chenxiuyi
 *
 */
public class DimentUtils {
	/**
	 * dip 转 px
	 * @param context
	 * @param dp
	 * @return
	 */
	public static float dipToPx(Context context,float dp){
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, metrics);
	}
	/**
	 * px 转 dip
	 * @param context
	 * @param px
	 * @return
	 */
	public static float pxToDip(Context context,float px){
		return px / context.getResources().getDisplayMetrics().density;
	}
	/**
	 * sp 转 px
	 * @param context
	 * @param sp
	 * @return
	 */
	public static float spTopx(Context context ,float sp){
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (sp * fontScale + 0.5f); 
	}

	/**
	 * 获取屏幕宽高
	 * @return 返回一个int数组，0宽度  1高度
	 */
	public static int[] getScreenSize(Context context){
		//初始化 屏幕尺寸参数
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int[] size = new int[2];
		size[0] = dm.widthPixels;
		size[1] = dm.heightPixels;
		return size;
	}
}
