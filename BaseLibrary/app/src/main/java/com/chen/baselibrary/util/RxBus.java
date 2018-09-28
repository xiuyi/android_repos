package com.chen.baselibrary.util;

import android.app.Instrumentation;
import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @author chen
 * @date 2018/9/28 下午5:34
 * email xiuyi.chen@erinspur.com
 * desc 时间总线工具类
 */

public class RxBus {
    private volatile static RxBus mDefaultInstance;
    private final Subject<Object> mBus;
    /**
     * 将订阅解绑绑定到Context，调用unSubscribe方法只解绑指定的Content的订阅
     */
    private Map<Context,CompositeDisposable> cxtDisposaleMap;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
        cxtDisposaleMap = new HashMap<>();
    }

    public static RxBus getInstance() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    /**
     * 订阅事件
     * @param context 绑定上下文
     * @param eventType 事件类型
     * @param consumer 事件消费者
     * @param <T> 事件类型
     */
    public <T> void subscribe(Context context,final Class<T> eventType,Consumer<T> consumer){
        mBus.ofType(eventType).subscribe(consumer, throwable -> {
            // onError
        }, () -> {
            // onComplete
        }, disposable -> {
            // onSubscribe
            CompositeDisposable compositeDisposable;
            if(cxtDisposaleMap.containsKey(context)){
                compositeDisposable = cxtDisposaleMap.get(context);
            }else{
                compositeDisposable = new CompositeDisposable();
                cxtDisposaleMap.put(context,compositeDisposable);
            }

            compositeDisposable.add(disposable);
        });
    }

    /**
     * 解除订阅，否则会造成内存溢出
     */
    public void unSubscribe(Context context){
        if(cxtDisposaleMap.containsKey(context)){
            CompositeDisposable compositeDisposable = cxtDisposaleMap.get(context);
            compositeDisposable.clear();
        }

        Logger.e("RxBus订阅对象列表数量:%d" + getSubscribeSize());
    }

    /**
     * 获取订阅的对象数量
     * @return
     */
    private int getSubscribeSize(){
        int i = 0;
        for(Map.Entry<Context,CompositeDisposable> entry:cxtDisposaleMap.entrySet()){
            i += entry.getValue().size();
        }
        return i;
    }
}
