package com.jgendeavors.rossquotes;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * The AlarmReceiver class handles the broadcast from AlarmManager to display a
 * notification message.
 */
public class AlarmReceiver extends BroadcastReceiver {
    // Constants
    private static final String TAG = "AlarmReceiver";
    // having some issues where db queries returned empty on the first few accesses
    //  as a workaround, we retry the query a maximum number of times
    public static final int MAX_DB_RETRIES = 10;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: started");
        // Alarm fired, so show a notification with a random quote from the database


        // Display a random Message from the db

        // Get a random Message from the database
        // get a list of all Messages in the database
        MessageRepository messageRepository = new MessageRepository(context);
        List<Message> messages = messageRepository.getAllSync();
        // first accesses return empty workaround
        int numRetries = 0;
        while (messages.isEmpty() && numRetries < MAX_DB_RETRIES) {
            messages = messageRepository.getAllSync();
            numRetries++;
        }
        if (messages.isEmpty()) {
            Log.e(TAG, "onReceive: found no Messages in db after " + MAX_DB_RETRIES + " queries. Canceling alarm and returning.");
            AlarmHelper.cancelAlarm(context);
            return;
        }
        // get a random Message from the list
        Random randy = new Random();
        int index = randy.nextInt(messages.size());
        Message message = messages.get(index);

        // Get the Contact associated with the Message
        ContactRepository contactRepository = new ContactRepository(context);
        Contact contact = contactRepository.getContactSync(message.getContactId());

        // Show a notification displaying the message

        // create the NotificationManager
        // use NotificationManagerCompat for displaying notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // Get Bitmap from Contact's image Uri to be used for notification's large icon
        //  based on this SO answer: https://stackoverflow.com/a/4717740
        Uri imgUri = Uri.parse(contact.getImageAbsolutePath());
        Bitmap largeIcon = null;
        try {
            largeIcon = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
        } catch (Exception e) {
            Log.e(TAG, "onReceive: ", e);
        }

        // create the notification
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_ID_MESSAGES)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(contact.getName())
                .setContentText(message.getText())
                .setLargeIcon(largeIcon)
                // set stuff here similar to our notification channel in App.java, to support APIs lower than O
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // display the notification
        notificationManager.notify(
                getNextNotificationId(context) /* id; can update this notification by sending another one with the same id. All our message notifications will have unique IDs */,
                notification
        );


        // Schedule another alarm 5 seconds from now
        // TODO schedule another alarm to be triggered with the set timeframe
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 5);
        AlarmHelper.setAlarm(context, c);
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
