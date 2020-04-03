package com.jgendeavors.rossquotes;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
                getNextNotificationId(context) /* id; can update this notification by sending another one with the same id. All our message notifications will have unique IDs */,
                notification
        );
    }


    // Private methods

    /**
     * Returns a unique integer to be used for message notification IDs.
     * Based on this SO answer: https://stackoverflow.com/a/41122403
     *
     * @param context
     * @return
     */
    private int getNextNotificationId(Context context) {
        // get the last ID to be stored in SharedPreferences and increment it
        final String PREF_LAST_NOTIFICATION_ID = "PREF_LAST_NOTIFICATION_ID";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int result = prefs.getInt(PREF_LAST_NOTIFICATION_ID, 0) + 1;
        if (result == Integer.MAX_VALUE) result = 0; // just to be sure!
        // update SharedPreferences
        prefs.edit().putInt(PREF_LAST_NOTIFICATION_ID, result).apply();

        return result;
    }
}
