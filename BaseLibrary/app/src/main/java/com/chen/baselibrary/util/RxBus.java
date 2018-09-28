package com.chen.baselibrary.util;

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
    private CompositeDisposable compositeDisposable;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
        compositeDisposable = new CompositeDisposable();
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
     * @param eventType 事件类型
     * @param consumer 事件消费者
     * @param <T> 事件类型
     */
    public <T> void subscribe(final Class<T> eventType,Consumer<T> consumer){
        mBus.ofType(eventType).subscribe(consumer, throwable -> {
            // onError
        }, () -> {
            // onComplete
        }, disposable -> {
            // onSubscribe
            compositeDisposable.add(disposable);
        });
    }

    /**
     * 解除订阅，否则会造成内存溢出
     */
    public void unSubscribe(){
        compositeDisposable.clear();
    }
}
