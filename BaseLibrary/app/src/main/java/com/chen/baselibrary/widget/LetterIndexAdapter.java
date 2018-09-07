package com.chen.baselibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.chen.baselibrary.util.DimentUtils;
import com.chen.baselibrary.util.LetterComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LetterIndexAdapter extends BaseAdapter implements
        SectionIndexer {

    private List<InitialLetter> data;
    private List<String> sections = new ArrayList<String>();
    private SparseIntArray seciontPosition;
    private Context context;
    private LetterComparator letterComparator;
    //字母标题的背景色
    private int topLetterBg = Color.parseColor("#ebebeb");
    //字母文字的颜色
    private int topLetterTextColor = Color.parseColor("#888888");
    //字母文字大小单位：sp
    private int topLetterTextSize = 12;
    //private LetterComparator comparator;

    public LetterIndexAdapter(Context contex, List<InitialLetter> data) {
        this.data = data;
        this.context = contex;
        this.letterComparator = new LetterComparator();
        if (data != null && data.size() > 0) {
            Collections.sort(data, letterComparator);
        }
        //this.comparator = new LetterComparator();
        //Collections.sort(this.data,this.comparator);
    }

    /**
     * 修改数据时调用该方法
     * @param  data:新的数据
     */
    public void refresh(List<InitialLetter> data) {
        this.data = data;
        Collections.sort(this.data, this.letterComparator);
        this.notifyDataSetChanged();
    }

    /**
     * 在元数据的基础上添加数据
     * @param data 需要添加的数据
     */
    public void addData(List<InitialLetter> data) {
        if(this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.addAll(data);
        Collections.sort(this.data, this.letterComparator);
        this.notifyDataSetChanged();
    }
    /**
     * 在元数据的基础上删除数据
     * @param data 需要删除的数据
     */
    public void removeData(List<InitialLetter> data) {
        if(this.data == null) {
            this.data = new ArrayList<>();
        }
        this.data.removeAll(data);
        Collections.sort(this.data, this.letterComparator);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public InitialLetter getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView topLetterView = null;
        if (convertView == null) {
            convertView = new LinearLayout(context);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(params);
            ((LinearLayout) convertView).setOrientation(LinearLayout.VERTICAL);
            TextView tv = new TextView(context);
            int w = LinearLayout.LayoutParams.MATCH_PARENT;
            int h = LinearLayout.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            tv.setText(getItem(position).getIntialLetter().toUpperCase());
            tv.setBackgroundColor(topLetterBg);
            tv.setTextColor(topLetterTextColor);
            tv.setTextSize(topLetterTextSize);
            tv.setPadding((int) DimentUtils.dipToPx(context,10), 5, 0, 5);
            tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            ((ViewGroup) convertView).addView(tv);
            topLetterView = tv;
            ((ViewGroup) convertView).addView(getContentView(position, null, parent));
        } else {
            TextView tv = (TextView) ((ViewGroup) convertView).getChildAt(0);
            topLetterView = tv;
            tv.setText(getItem(position).getIntialLetter().toUpperCase());
            getContentView(position, ((ViewGroup) convertView).getChildAt(1), parent);
        }
        if (position > 0) {
            if (!getItem(position).getIntialLetter().equalsIgnoreCase(getItem(position - 1).getIntialLetter())) {
                topLetterView.setVisibility(View.VISIBLE);
            } else {
                topLetterView.setVisibility(View.GONE);
            }
        }else if(position == 0){
            topLetterView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public abstract View getContentView(int position, View convertView, ViewGroup parent);

    @Override
    public int getPositionForSection(int sectionIndex) {
        return seciontPosition.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        sections.clear();
        sections.add("搜");
        seciontPosition = new SparseIntArray();
        seciontPosition.put(0, 0);
        for (int i = 0; i < data.size(); i++) {
            InitialLetter item = data.get(i);
            if (!sections.get(sections.size() - 1).equals(
                    item.getIntialLetter())) {
                sections.add(item.getIntialLetter());
                seciontPosition.put(sections.size() - 1, i);
            }
        }
        /*for (int i = 0; i < seciontPosition.size(); i++) {
			int key = seciontPosition.keyAt(i);
			// get the object by the key.
			int obj = seciontPosition.get(key);
		}*/
        return sections.toArray(new String[sections.size()]);
    }
}
