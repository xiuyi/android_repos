package com.chen.baselibrary.util;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
     * 将订阅解绑绑定到Context，调用unSubscribe方法只解绑指定的Context的订阅
     * 此处使用弱引用Context，防止Context销毁时此处被强引用无法回收Context
     */
    private WeakHashMap<Context, CompositeDisposable> cxtDisposaleMap;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
        //在当前线程发布事件
        mBus.subscribeOn(Schedulers.trampoline());
        cxtDisposaleMap = new WeakHashMap<>();
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
     * 订阅事件,在当前线程
     *
     * @param context   绑定上下文
     * @param eventType 事件类型
     * @param consumer  事件消费者 只处理onNext事件
     * @param <T>       事件类型
     */
    public <T> void subscribe(Context context, Class<T> eventType, Consumer<T> consumer) {
        this.subscribe(context,Schedulers.trampoline(),eventType,consumer);
    }
    /**
     * 订阅事件,在主线程
     *
     * @param context   绑定上下文
     * @param eventType 事件类型
     * @param consumer  事件消费者 只处理onNext事件
     * @param <T>       事件类型
     */
    public <T> void subscribeOnMain(Context context, Class<T> eventType, Consumer<T> consumer) {
        this.subscribe(context,AndroidSchedulers.mainThread(),eventType,consumer);
    }
    /**
     * 订阅事件,在IO线程
     *
     * @param context   绑定上下文
     * @param eventType 事件类型
     * @param consumer  事件消费者 只处理onNext事件
     * @param <T>       事件类型
     */
    public <T> void subscribeOnIO(Context context, Class<T> eventType, Consumer<T> consumer) {
        this.subscribe(context,Schedulers.io(),eventType,consumer);
    }
    /**
     * 订阅事件,在新线程
     *
     * @param context   绑定上下文
     * @param eventType 事件类型
     * @param consumer  事件消费者 只处理onNext事件
     * @param <T>       事件类型
     */
    public <T> void subscribeOnNewThread(Context context, Class<T> eventType, Consumer<T> consumer) {
        this.subscribe(context,Schedulers.newThread(),eventType,consumer);
    }
    /**
     * 订阅事件,在computation线程
     *
     * @param context   绑定上下文
     * @param eventType 事件类型
     * @param consumer  事件消费者 只处理onNext事件
     * @param <T>       事件类型
     */
    public <T> void subscribeOnComputation(Context context, Class<T> eventType, Consumer<T> consumer) {
        this.subscribe(context,Schedulers.computation(),eventType,consumer);
    }
    /**
     * 统一的订阅方法，私有
     * @param context
     * @param scheduler
     * @param eventType
     * @param consumer
     * @param <T>
     */
    private <T> void subscribe(Context context, Scheduler scheduler, Class<T> eventType, Consumer<T> consumer){
        mBus.ofType(eventType)
                .observeOn(scheduler)
                .subscribe(consumer, throwable -> {
                    // onError
                }, () -> {
                    // onComplete
                }, disposable -> {
                    // onSubscribe
                    CompositeDisposable compositeDisposable;
                    if (cxtDisposaleMap.containsKey(context)) {
                        compositeDisposable = cxtDisposaleMap.get(context);
                    } else {
                        compositeDisposable = new CompositeDisposable();
                        cxtDisposaleMap.put(context, compositeDisposable);
                    }

                    compositeDisposable.add(disposable);
                });
    }
    /**
     * 解除订阅，否则会造成内存溢出
     */
    public void unSubscribe(Context context) {
        if (cxtDisposaleMap.containsKey(context)) {
            CompositeDisposable compositeDisposable = cxtDisposaleMap.get(context);
            compositeDisposable.clear();
        }
        int subscribeSize = getSubscribeSize();
        System.out.println("RxBus订阅对象列表数量:" + subscribeSize);
        Logger.i("RxBus订阅对象列表数量:%d", subscribeSize);
    }

    /**
     * 获取订阅的对象数量
     *
     * @return
     */
    private int getSubscribeSize() {
        int i = 0;
        for (Map.Entry<Context, CompositeDisposable> entry : cxtDisposaleMap.entrySet()) {
            i += entry.getValue().size();
        }
        return i;
    }
}
