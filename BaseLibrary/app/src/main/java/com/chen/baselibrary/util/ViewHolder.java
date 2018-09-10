package com.chen.baselibrary.util;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder工具类 系统改用黄油刀绑定View，不在推荐使用该ViewHolder
 * @author chenxiuyi
 * 使用方法
 *public View getView(int position, View convertView, ViewGroup parent) {  
    if (convertView == null) {  
        convertView = LayoutInflater.from(context)  
          .inflate(R.layout.banana_phone, parent, false);  
    }  
   
    ImageView bananaView = ViewHolder.get(convertView, R.id.banana);  
    TextView phoneView = ViewHolder.get(convertView, R.id.phone);  
   
    BananaPhone bananaPhone = getItem(position);  
    phoneView.setText(bananaPhone.getPhone());  
    bananaView.setImageResource(bananaPhone.getBanana());  
   
    return convertView;  
}  
 */
@Deprecated
public class ViewHolder {
	// 使用泛型降低类型转换
    @SuppressWarnings("unchecked")  
    public static <T extends View> T get(View view, int id) {  
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();  
        if (viewHolder == null) {  
            viewHolder = new SparseArray<View>();  
            view.setTag(viewHolder);  
        }  
        View childView = viewHolder.get(id);  
        if (childView == null) {  
            childView = view.findViewById(id);  
            viewHolder.put(id, childView);  
        }  
        return (T) childView;  
    }  
}
