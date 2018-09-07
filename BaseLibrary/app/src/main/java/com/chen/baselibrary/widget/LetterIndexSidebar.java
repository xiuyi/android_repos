/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chen.baselibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chen.baselibrary.R;
import com.chen.baselibrary.util.DimentUtils;


/**
 *右侧字母索引
 * @author chenxiuyi
 *
 */
public class LetterIndexSidebar extends View{
	private Paint paint;
	private TextView header;
	private float height;
	private ListView mListView;
	private Context context;
	private LetterChangeListener letterChangeListener;
	
	private SectionIndexer sectionIndexter = null;
	
	public void setListView(ListView listView){
		mListView = listView;
	}
	

	public LetterIndexSidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private String[] sections; 

	private void init(){
	    String st = "↑";
        sections= new String[]{st,"A","B","C","D","E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","#"};
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//索引文字颜色
		paint.setColor(Color.parseColor("#717171"));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(DimentUtils.spTopx(context, 10));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float center = getWidth() / 2;
		height = getHeight() / sections.length;
		for (int i = sections.length - 1; i > -1; i--) {
			canvas.drawText(sections[i], center, height * (i+1), paint);
		}
	}
	
	private int sectionForPoint(float y) {
		int index = (int) (y / height);
		if(index < 0) {
			index = 0;
		}
		if(index > sections.length - 1){
			index = sections.length - 1;
		}
		return index;
	}
	
	private void setHeaderTextAndscroll(MotionEvent event){
		 if (mListView == null) {
		        //check the mListView to avoid NPE. but the mListView shouldn't be null
		        //need to check the call stack later
		        return;
		    }
		String headerString = sections[sectionForPoint(event.getY())];
		if(this.letterChangeListener!=null)
			this.letterChangeListener.change(headerString);
		ListAdapter adapter = mListView.getAdapter();
		if(sectionIndexter == null){
    		if(adapter instanceof HeaderViewListAdapter){
    		    sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
    		}else if(adapter instanceof SectionIndexer){
    		    sectionIndexter = (SectionIndexer)adapter;
    		}else{
    		    throw new RuntimeException("apapter 必须实现SectionIndexer接口");
    		}
		}
		String[] adapterSections = (String[]) sectionIndexter.getSections();
		try {
			for (int i = adapterSections.length - 1; i > -1; i--) {
				if(adapterSections[i].equalsIgnoreCase(headerString)){
					int position = sectionIndexter.getPositionForSection(i);
					System.out.println(position+"======================");
					mListView.setSelection(position);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("setHeaderTextAndscroll", e.getMessage());
		}
		
	}
	/**
	 * 获取索引改变的监听
	 */
	public LetterChangeListener getLetterChangeListener() {
		return letterChangeListener;
	}
	/**
	 * 设置索引改变的监听
	 * @param letterChangeListener
	 */
	public void setLetterChangeListener(LetterChangeListener letterChangeListener) {
		this.letterChangeListener = letterChangeListener;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			if(this.letterChangeListener!=null)
				this.letterChangeListener.startChange();
			setHeaderTextAndscroll(event);
			setBackgroundResource(R.drawable.shape_transparent_black_c5dp_trbl);
			return true;
		}
		case MotionEvent.ACTION_MOVE:{
			setHeaderTextAndscroll(event);
			return true;
		}
		case MotionEvent.ACTION_UP:
			if(this.letterChangeListener!=null)
				this.letterChangeListener.stopChange();
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		case MotionEvent.ACTION_CANCEL:
			if(this.letterChangeListener!=null)
				this.letterChangeListener.stopChange();
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		}
		return super.onTouchEvent(event);
	}
	/**
	 * 字母索引改变的监听
	 */
	public interface LetterChangeListener{
		void startChange();
		void change(String letter);
		void stopChange();
	}
}
