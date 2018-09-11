package com.chen.baselibrary.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author chen
 * @date 2018/9/10 下午4:23
 * email xiuyi.chen@erinspur.com
 * desc Activity工具类
 */

public class ActivityUtils {
    /**
     * Activity添加Fragment
     * @param fragmentManager
     * @param fragment
     * @param frameId 容器ID
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * 功能描述:简单地Activity的跳转(不携带任何数据)
     *
     * @param activity 发起跳转的Activity实例
     * @param cls 目标Activity实例
     * @param srcFinish 是否需要销毁源activity
     */
    public static void jumpToActivity(Activity activity,
                                      Class<? extends Activity> cls,boolean srcFinish) {
        checkNotNull(activity);
        checkNotNull(cls);
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        if(srcFinish) {
            activity.finish();
        }
    }

    /**
     * 功能描述：带数据的Activity之间的跳转
     *
     * @param activity
     * @param cls
     * @param hashMap
     * @param srcFinish 是否需要销毁源activity
     */
    public static void jumpToActivity(Activity activity,
                                      Class<? extends Activity> cls,
                                      HashMap<String,Object> hashMap, boolean srcFinish) {
        checkNotNull(activity);
        checkNotNull(cls);
        Intent intent = new Intent(activity, cls);
        Iterator<?> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                intent.putExtra(key, (String) value);
            }else if (value instanceof Boolean) {
                intent.putExtra(key, (boolean) value);
            }else if (value instanceof Integer) {
                intent.putExtra(key, (int) value);
            }else if (value instanceof Float) {
                intent.putExtra(key, (float) value);
            }else if (value instanceof Double) {
                intent.putExtra(key, (double) value);
            }else if (value instanceof Character) {
                intent.putExtra(key, (Character) value);
            }else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            }else if (value instanceof Parcelable){
                intent.putExtra(key , (Parcelable) value);
            }else if (value instanceof Serializable){
                intent.putExtra(key , (Serializable) value);
            }else{
                throw new IllegalArgumentException(value.toString() + "类型不支持放入Intent.putExtra()");
            }
        }
        activity.startActivity(intent);

        if(srcFinish) {
            activity.finish();
        }
    }
}
