package com.jgendeavors.rossquotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Helper class for broadcasting alarms with AlarmManager
 */
public class AlarmHelper {

    /**
     * Broadcasts an alarm set to @calendar's time to be received by our AlarmReceiver class.
     *
     * @param context
     * @param calendar
     */
    public static void setAlarm(Context context, Calendar calendar) {
        // Create the intent the alarm will broadcast
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1 /* requestCode, must be unique for each different PendingIntent. We'll have at most one PendingIntent at any time. */,
                intent,
                0 /* flags */
        );

        // Set alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Cancels the alarm if it is set.
     *
     * @param context
     */
    public static void cancelAlarm(Context context) {
        // Set up the Intents and AlarmManager exactly as in setAlarm()
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1 /* requestCode, must be unique for each different PendingIntent. We'll have at most one PendingIntent at any time. */,
                intent,
                0 /* flags */
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel alarm
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}
