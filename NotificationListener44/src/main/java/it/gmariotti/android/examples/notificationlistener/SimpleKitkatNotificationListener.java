package it.gmariotti.android.examples.notificationlistener;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleKitkatNotificationListener extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        //android.os.Debug.waitForDebugger();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification=sbn.getNotification();
        if (mNotification!=null){
            Bundle extras = mNotification.extras;

            Intent intent = new Intent(MainActivity.INTENT_ACTION_NOTIFICATION);
            intent.putExtras(mNotification.extras);
            sendBroadcast(intent);

            Notification.Action[] mActions=mNotification.actions;
            if (mActions!=null){
                for (Notification.Action mAction:mActions){
                    int icon=mAction.icon;
                    CharSequence actionTitle=mAction.title;
                    PendingIntent pendingIntent=mAction.actionIntent;
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }
}
