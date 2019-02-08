package xyz.rty813.gfremind;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

public class NotificationUtils {

    public static void setChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(new NotificationChannel("1", "重要消息", NotificationManager.IMPORTANCE_HIGH));
        notificationManager.createNotificationChannel(new NotificationChannel("2", "TIM消息", NotificationManager.IMPORTANCE_DEFAULT));
        notificationManager.createNotificationChannel(new NotificationChannel("3", "聊天时间提醒", NotificationManager.IMPORTANCE_DEFAULT));
    }

    public static void notifyMsg(Context context, String packageName, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, title.contains("姜姜") ? "1": "2");
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setColorized(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        notificationManager.notify(packageName.hashCode(), notification);
    }

    public static void cancel(Context context, String tag, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (tag == null) {
            notificationManager.cancel(id);
        }
        else {
            notificationManager.cancel(tag, id);
        }
    }
}
