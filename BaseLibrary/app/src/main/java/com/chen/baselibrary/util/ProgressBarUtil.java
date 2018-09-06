package com.chen.baselibrary.util;

import android.content.Context;

import com.chen.baselibrary.widget.SimpleProgressDialog;


public class ProgressBarUtil {

	private static ProgressBarUtil instance;
	private SimpleProgressDialog progressDialog;
	
	private ProgressBarUtil(){}
	
	public static synchronized ProgressBarUtil getInstance(){
		if(instance==null)
			instance = new ProgressBarUtil();
		return instance;
	}
	/**
	 * 显示进度条，如果text为null，则隐藏文字
	 * @param context
	 * @param text
	 */
	public void showProgressBar(Context context,String text){
		if(this.progressDialog==null)
			this.progressDialog = new SimpleProgressDialog(context, text);
		this.progressDialog.setCanceledOnTouchOutside(false);
		if (!this.progressDialog.isShowing())
			this.progressDialog.show();
	}
	/**
	 * 移除进度条
	 */
	public void removeProgressBar(){
		if(this.progressDialog!=null && this.progressDialog.isShowing()){
			this.progressDialog.cancel();
		}
		this.progressDialog = null;	
	}
}
