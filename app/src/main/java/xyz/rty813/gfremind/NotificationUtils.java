package xyz.rty813.gfremind;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {

    public static void setChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("1", "重要消息", NotificationManager.IMPORTANCE_HIGH));
            notificationManager.createNotificationChannel(new NotificationChannel("2", "普通消息", NotificationManager.IMPORTANCE_DEFAULT));
            notificationManager.createNotificationChannel(new NotificationChannel("3", "聊天时间提醒", NotificationManager.IMPORTANCE_DEFAULT));
            notificationManager.createNotificationChannel(new NotificationChannel("4", "正在监听", NotificationManager.IMPORTANCE_MIN));
        }
    }

    public static void notifyMsg(Context context, String packageName, String title, String content, String channel, boolean hide, PendingIntent contentIntent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, channel);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder.setContentTitle(hide ? packageName : title)
                .setContentText(hide ? "收到一条通知" : content)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setColorized(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(packageName.hashCode(), notification);
    }

    static void cancelMsg(Context context, String packageName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(packageName.hashCode());
    }
}
