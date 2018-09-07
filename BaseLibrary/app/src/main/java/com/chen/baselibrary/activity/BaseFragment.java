package com.chen.baselibrary.activity;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author chen
 * @date 2018/9/7 下午2:11
 * email xiuyi.chen@erinspur.com
 * desc Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    protected Unbinder unbinder;

    public BaseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getContentView(inflater, container, savedInstanceState);
        // 检查非空
        view = checkNotNull(view);
        // 绑定View
        this.unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在fragment中必须调用unbind方法
        this.unbinder.unbind();
    }

    /**
     * 有子类实现的抽象方法，获取view
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @NonNull
    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState);

}
