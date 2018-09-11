package com.chen.baselibrary.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.chen.baselibrary.activity.BaseApplication;

import static com.google.common.base.Preconditions.checkState;
/**
 * @author chen
 * @date 2018/9/10 下午5:39
 * email xiuyi.chen@erinspur.com
 * desc 通知工具类 单例
 */

public class NotificationUtils {
    private static final String TAG = NotificationUtils.class.getSimpleName();
    private static final NotificationUtils instance = new NotificationUtils();
    private NotificationUtils(){}
    /**
     * 是否初始化完成
     */
    private boolean isInit = false;
    /**
     * 默认的channel_ID
     */
    private String DEFAULT_CHANNEL_ID = SystemUtils.getAppId();

    public static final NotificationUtils getInstance(){
        return instance;
    }

    /**
     * 初始化方法
     * @param channels 兼容Version.O(8.0) 创建channels，如果为null
     *                 默认创建以APPID为CHANNEL_ID的channel
     */
    public void init(NotificationChannel... channels){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //默认APPID的channel_id
            String channelId = DEFAULT_CHANNEL_ID;
            String channelName = SystemUtils.getAppName();
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel defaultChannel = new NotificationChannel(channelId, channelName, importance);

            NotificationChannel[] newChannelsArr;
            if(channels != null && channels.length > 0){
                newChannelsArr = new NotificationChannel[channels.length + 1];
                System.arraycopy(channels,0,newChannelsArr,0,channels.length);
            }else{
                newChannelsArr = new NotificationChannel[]{defaultChannel};
            }

            NotificationManager notificationManager = BaseApplication.getInstance().getSystemService(NotificationManager.class);
            for(NotificationChannel nc : newChannelsArr){
                notificationManager.createNotificationChannel(nc);
                Log.i(TAG,"保存Notification_ChannelId : " + nc.getId());
            }
        }

        this.isInit = true;
    }

    /**
     * 使用默认的CHANNEL_ID构造通知
     * @param context
     * @param smallIcon
     * @param defaults
     * @param ticker
     * @param title
     * @param content
     * @param contentInfo
     * @param autoCancel
     * @param clickIntent
     * @return
     */
    public Notification buildNotificationByDefaultChannelId(Context context,int smallIcon, int defaults, String ticker, String title, String content, String contentInfo, boolean autoCancel,PendingIntent clickIntent){
        return this.buildNotification(context,DEFAULT_CHANNEL_ID,smallIcon,defaults,ticker,title,content,contentInfo,autoCancel,null,clickIntent);
    }
    /**
     * 私有的全参通知构造方法
     * @param context
     * @param channelId
     * @param smallIcon
     * @param defaults
     * @param ticker
     * @param title
     * @param content
     * @param contentInfo
     * @param autoCancel
     * @param deleteIntent
     * @param clickIntent
     * @return Notification
     */
    private Notification buildNotification(Context context, String channelId, int smallIcon, int defaults, String ticker, String title, String content, String contentInfo, boolean autoCancel, PendingIntent deleteIntent,PendingIntent clickIntent) {
        checkState(this.isInit,"NotificationUtils未初始化");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setDefaults(defaults);

        if(!TextUtils.isEmpty(ticker)){
            builder.setTicker(ticker);
        }
        if(!TextUtils.isEmpty(title)){
            builder.setContentTitle(title);
        }
        if(!TextUtils.isEmpty(content)){
            builder.setContentText(content);
        }
        if(!TextUtils.isEmpty(contentInfo)){
            builder.setContentInfo(contentInfo);
        }
        if(deleteIntent != null){
            builder.setDeleteIntent(deleteIntent);
        }
        if(clickIntent != null){
            builder.setContentIntent(clickIntent);
        }
        return builder.build();
    }
}
