package com.jgendeavors.rossquotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

/**
 * Helper class for broadcasting alarms with AlarmManager
 */
public class AlarmHelper {
    private static final String TAG = "AlarmHelper";

    /**
     * Broadcasts an alarm set to @calendar's time to be received by our AlarmReceiver class.
     *
     * @param context
     * @param calendar
     */
    public static void setAlarm(Context context, Calendar calendar) {
        Log.d(TAG, "setAlarm: started. Calendar set to " + calendar.getTime());
        // Create the intent the alarm will broadcast
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1 /* requestCode, must be unique for each different PendingIntent. We'll have at most one PendingIntent at any time. */,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT /* flags */
        );

        // Set alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Picks a random time between @minMillisFromNow and @maxMillisFromNow milliseconds from the current time
     * and calls setAlarm(Context, Calendar) and passes the randomly selected time.
     *
     * @param context
     * @param minMillisFromNow
     * @param maxMillisFromNow
     */
    public static void setAlarm(Context context, int minMillisFromNow, int maxMillisFromNow) {
        // Get a random value between min and max
        Random randy = new Random();
        // we add 1 to the bound to make the max value inclusive, and allow for min and max to be equal
        int millisFromNow = randy.nextInt(maxMillisFromNow - minMillisFromNow + 1) + minMillisFromNow;

        // Set a Calendar to the randomly selected number of millis in the future
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, millisFromNow);

        // Set the alarm with the Calendar
        setAlarm(context, c);
    }

    /**
     * Cancels the alarm if it is set.
     *
     * @param context
     */
    public static void cancelAlarm(Context context) {
        Log.d(TAG, "cancelAlarm: started");
        // Set up the Intents and AlarmManager exactly as in setAlarm()
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1 /* requestCode, must be unique for each different PendingIntent. We'll have at most one PendingIntent at any time. */,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT /* flags */
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Cancel alarm
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    /**
     * Returns true if the alarm to send a notification has been set, false otherwise.
     * Based on this SO answer: https://stackoverflow.com/a/28076168
     *
     * @param context
     * @return
     */
    public static boolean isSet(Context context) {
        // Set up the Intent the same as in setAlarm()
        Intent intent = new Intent(context, AlarmReceiver.class);
        // Set up the PendingIntent as in setAlarm(), except with the FLAG_NO_CREATE flag
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_NO_CREATE /* returns null if the PendingIntent doesn't exist, instead of creating it */
        );

        return pendingIntent != null;
    }

}
