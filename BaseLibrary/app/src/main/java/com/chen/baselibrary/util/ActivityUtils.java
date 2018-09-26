package com.chen.baselibrary.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private static final String FRAGMENT_TAG = "MyActivityResultFragment";
    private static final int REQUEST_CODE = 0x9009;
    private  MyActivityResultFragment myActivityResultFragment;

    public ActivityUtils(@NonNull Activity activity){
        this.myActivityResultFragment = getResultFragment(activity);
    }
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
     * Fragment跳转到Activity（不带参数）
     * @param fragment
     * @param cls
     * @param srcFinish
     */
    public static void jumpToActivity(Fragment fragment,
                                      Class<? extends Activity> cls,boolean srcFinish) {
        checkNotNull(fragment);
        checkNotNull(cls);
        Activity activity = fragment.getActivity();
        jumpToActivity(activity,cls,srcFinish);
    }

    /**
     * Fragment跳转到Activity（携带参数)
     * @param fragment
     * @param cls
     * @param hashMap
     * @param srcFinish
     */
    public static void jumpToActivity(Fragment fragment,
                                      Class<? extends Activity> cls,
                                      HashMap<String,Object> hashMap, boolean srcFinish) {
        checkNotNull(fragment);
        checkNotNull(cls);
        Activity activity = fragment.getActivity();
       jumpToActivity(activity,cls,hashMap,srcFinish);
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
        jumpToActivity(activity,cls,null,srcFinish);
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
        if(hashMap != null) {
            Iterator<?> iterator = hashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("unchecked")
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (boolean) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (int) value);
                } else if (value instanceof Float) {
                    intent.putExtra(key, (float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (double) value);
                } else if (value instanceof Character) {
                    intent.putExtra(key, (Character) value);
                } else if (value instanceof Long) {
                    intent.putExtra(key, (Long) value);
                } else if (value instanceof Parcelable) {
                    intent.putExtra(key, (Parcelable) value);
                } else if (value instanceof Serializable) {
                    intent.putExtra(key, (Serializable) value);
                } else {
                    throw new IllegalArgumentException(value.toString() + "类型不支持放入Intent.putExtra()");
                }
            }
        }
        activity.startActivity(intent);

        if(srcFinish) {
            activity.finish();
        }
    }

    /**
     * startActivityForResult
     * @param activity 源Activity
     * @param requestIntent 目标intent
     * @param callback 回调
     */
    public void startActivityForResult(Activity activity,Intent requestIntent,ActivityResultCallback callback){
        checkNotNull(myActivityResultFragment);
        this.myActivityResultFragment.setResultCallback(callback);
        this.myActivityResultFragment.startActivityForResult(requestIntent,REQUEST_CODE);
    }

    /**
     * 获取 MyActivityResultFragment 实例
     * @param activity
     * @return
     */
    private MyActivityResultFragment getResultFragment(Activity activity) {
        MyActivityResultFragment myActivityResultFragment = findResultFragment(activity);
        boolean isNewInstance = myActivityResultFragment == null;
        if (isNewInstance) {
            myActivityResultFragment = new MyActivityResultFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(myActivityResultFragment, FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return myActivityResultFragment;
    }

    private MyActivityResultFragment findResultFragment(Activity activity) {
        return (MyActivityResultFragment) activity.getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }


    /**
     * 一个辅助的Fragment，用于启动StartActivityForResult
     * 所用调用startActivityForResult方法启动的Activity实际
     * 将被该隐藏的Fragment启动，并在该Fragment中回调
     */
    public static class MyActivityResultFragment extends Fragment{
        private ActivityResultCallback callback;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(this.callback != null){
                if(resultCode == Activity.RESULT_CANCELED) {
                    this.callback.onCanceledResult();
                }else{
                    this.callback.onOkResult(data);
                }
            }
        }

        public void setResultCallback(ActivityResultCallback callback) {
            this.callback = callback;
        }
    }

    /**
     * ActivityResult回调接口
     */
    public interface ActivityResultCallback{
        /**
         * resultAcitivt被取消RESULT_CANCELED
         */
        void onCanceledResult();

        /**
         * resultActivity正常返回RESULT_OK或其他
         * @param data
         */
        void onOkResult(Intent data);
    }
}
