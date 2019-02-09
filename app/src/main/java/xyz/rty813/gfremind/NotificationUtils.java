package xyz.rty813.gfremind;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {

    public static void setChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(new NotificationChannel("1", "重要消息", NotificationManager.IMPORTANCE_HIGH));
        notificationManager.createNotificationChannel(new NotificationChannel("2", "普通消息", NotificationManager.IMPORTANCE_DEFAULT));
        notificationManager.createNotificationChannel(new NotificationChannel("3", "聊天时间提醒", NotificationManager.IMPORTANCE_DEFAULT));
    }

    public static void notifyMsg(Context context, String packageName, String title, String content, String channel) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, channel);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Intent intent = new Intent(context, DetailActivity.class).putExtra("package", packageName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setContentTitle(channel.equals("1") ? packageName : title)
                .setContentText(channel.equals("1") ? "收到一条通知" : content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
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
