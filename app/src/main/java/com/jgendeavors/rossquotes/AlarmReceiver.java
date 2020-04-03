package com.jgendeavors.rossquotes;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * The AlarmReceiver class handles the broadcast from AlarmManager to display a
 * notification message.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Alarm fired, so show a notification with a random quote from the database

        // TODO
        //  display a random quote from db
        //  schedule alarm again for a random time within timeframe

        // TODO each new message should be a separate notification, right?

        // create the NotificationManager
        // use NotificationManagerCompat for displaying notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // create the notification
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID_MESSAGES)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Alert message right here")
                .setContentText("Alarm fired!")
                // set stuff here similar to our notification channel in App.java, to support APIs lower than O
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // display the notification
        notificationManager.notify(
                1 /* id; can update this notification by sending another one with the same id*/,
                notification
        );
    }
}
