package com.chen.baselibrary.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * @author chen
 * @date 2018/9/10 下午5:39
 * email xiuyi.chen@erinspur.com
 * desc 通知工具类
 */

public class NotificationUtils {
    /**
     * 构建通知
     * @param context
     * @param ticker
     * @param title
     * @param content
     * @param contentInfo
     * @return
     */
    public static Notification buildNotification(Context context, int smallIcon, String ticker, String title, String content, String contentInfo, boolean autoCancel, PendingIntent deleteIntent,PendingIntent clickIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getPackageName());
        builder.setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo(contentInfo)
                .setDeleteIntent(deleteIntent)
                .setContentIntent(clickIntent);
        return builder.build();
    }
}
