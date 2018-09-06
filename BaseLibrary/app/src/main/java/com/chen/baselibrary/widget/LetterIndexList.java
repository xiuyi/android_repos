package com.chen.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chen.baselibrary.R;


/**
 * 带有字母索引的listview
 * 使用方法：
 * 1、定义布局文件：com.chen.baselibrary.widget.LetterIndexList
 * 2、创建Adapter继承自LetterIndexAdapter
 * 3、setAdapter（）
 * 4、更新数据时可以使用adapter.refresh(List<InitialLetter> data)方法更新
 * 5、添加数据时可以使用adapter.addData(List<InitialLetter> data)方法添加
 * 6、注意：adapter中已经进行了排序，使用时无需再次排序
 * 7、自定义颜色：修改标题文字和背景色需要修改LetterIndexAdapter类的属性颜色，修改索引的文字颜色需要修改
 * LetterIndexSidebar第68行颜色值
 * @author chenxiuyi
 */
public class LetterIndexList extends RelativeLayout {
	private ListView listView;
	private TextView floatTv;
	private LetterIndexAdapter adapter;
	private LayoutInflater inflater;
	private LetterIndexSidebar sideBar;
	private View headerView;

	public LetterIndexList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LetterIndexList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LetterIndexList(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.widget_letter_list, this);
		this.sideBar = (LetterIndexSidebar) findViewById(R.id.side_bar);
		this.listView = (ListView) findViewById(R.id.lv_list);
		this.floatTv = (TextView) findViewById(R.id.float_tv);
		this.sideBar.setVisibility(View.VISIBLE);
		this.sideBar.setListView(listView);
		this.sideBar.setLetterChangeListener(new LetterIndexSidebar.LetterChangeListener() {
			
			@Override
			public void change(String letter) {
				floatTv.setText(letter);
			}

			@Override
			public void startChange() {
				floatTv.setVisibility(View.VISIBLE);
			}

			@Override
			public void stopChange() {
				floatTv.setVisibility(View.GONE);
			}
		});
	}
	/**
	 * 设置adapter必须继承ListAdapter，重写getView()方法即可
	 * @param adapter
	 */
	public void setAdapter(LetterIndexAdapter adapter){
		this.adapter = adapter;
		this.listView.setAdapter(adapter);
	}

	/**
	 * 通过ListView的addHeaderView方法添加一个Header View
	 */
	public void setHeaderView(View v){
		if(this.listView != null){
			this.headerView = v;
			this.listView.addHeaderView(v);
		}
	}

	/**
	 * 清空headerView
	 */
	public void clearHeaderView(){
		if(this.listView != null && this.headerView != null){
			this.listView.removeHeaderView(headerView);
		}
	}
	/**
	 * 刷新数据
	 */
	public void refresh(){
		if(this.adapter!=null) {
			this.adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 获取内部ListView
	 * @return
	 */
	public ListView getListView(){
		return this.listView;
	}
}
