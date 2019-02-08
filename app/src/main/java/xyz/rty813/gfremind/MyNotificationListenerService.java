package xyz.rty813.gfremind;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class MyNotificationListenerService extends NotificationListenerService {
    private final static String TAG = "NotificationListenerService";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null || sbn.getPackageName().equals("xyz.rty813.gfremind")) {
            return;
        }
        printNotification(sbn);
        Bundle bundle = notification.extras;
        String packageName = sbn.getPackageName();
        String title = bundle.getString(Notification.EXTRA_TITLE);
        String content = bundle.getString(Notification.EXTRA_TEXT);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = df.format(new Date());

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("package", packageName);
        values.put("time", time);
        MainActivity.sqliteOperate.insert("notification", values);
        sendBroadcast(new Intent(MainActivity.ACTION_NOTIFICATION_RECEIVE)
                .putExtra("values", values));

        if (sbn.getPackageName().endsWith("com.tencent.tim")) {
            NotificationUtils.notifyMsg(this, sbn.getPackageName(),
                    bundle.getString(Notification.EXTRA_TITLE), notification.extras.getString(Notification.EXTRA_TEXT));
            NotificationUtils.cancel(this, sbn.getTag(), sbn.getId());
        }
    }

    private void printNotification(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        // 标题和时间
        String title = "";
        if (notification.tickerText != null) {
            title = notification.tickerText.toString();
        }
        long when = notification.when;

        Bundle bundle = notification.extras;
        // 内容标题、内容、副内容
        String contentTitle = bundle.getString(Notification.EXTRA_TITLE);
        if (contentTitle == null) {
            contentTitle = "";
        }
        String contentText = bundle.getString(Notification.EXTRA_TEXT);
        if (contentText == null) {
            contentText = "";
        }
        String contentSubtext = bundle.getString(Notification.EXTRA_SUB_TEXT);
        if (contentSubtext == null) {
            contentSubtext = "";
        }
        Log.i(TAG, "notify msg: packageName=" + sbn.getPackageName() + ", title=" + title + " ,when=" + when
                + " ,contentTitle=" + contentTitle + " ,contentText="
                + contentText + " ,contentSubtext=" + contentSubtext);
    }

    public static boolean isNotificationListenerServiceEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageNames.contains(context.getPackageName());
    }
}
