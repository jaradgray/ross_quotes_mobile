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

        // get a random enabled Contact from the database
        Contact contact = getRandomEnabledContact(context);
        // if we found an enabled Contact, get a random Message with the Contact's id
        Message message = null;
        if (contact != null) {
            message = getRandomMessageForContact(context, contact.getId());
        }

        // Show a notification displaying the message
        //  handle no enabled Contacts found case
        //  handle no Messages found for Contact case

        // get a Notification to display
        Notification notification;
        if (contact == null) {
            // no enabled Contact found
            notification = getNoEnabledContactsNotification(context);
        } else if (message == null) {
            // enabled Contact found, but no Messages with Contact's id
            notification = getNoMessagesForContactNotification(context, contact);
        } else {
            // normal case
            notification = getContactMessageNotification(context, contact, message);
        }
        // create the NotificationManager
        //  use NotificationManagerCompat for displaying notifications
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // display the notification
        notificationManager.notify(
                getNextNotificationId(context) /* id; can update this notification by sending another one with the same id. All our message notifications will have unique IDs */,
                notification
        );

        // Schedule another alarm to be triggered within the persisted timeframe, if the app is enabled
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isEnabled = prefs.getBoolean(App.PREF_KEY_APP_ENABLED, true);
        if (isEnabled) {
            AlarmHelper.setAlarmWithinPersistedTimeframe(context);
        }
    }


    // Private methods

    /**
     * Returns a randomly chosen enabled Contact from the database, or null if none were found.
     *
     * @param context
     * @return
     */
    private Contact getRandomEnabledContact(Context context) {
        // Get all enabled Contacts from the database
        ContactRepository contactRepository = new ContactRepository(context);
        List<Contact> enabledContacts = contactRepository.getAllEnabledSync();

        // First accesses return empty workaround
        int numRetries = 0;
        while (enabledContacts.isEmpty() && numRetries < MAX_DB_RETRIES) {
            enabledContacts = contactRepository.getAllEnabledSync();
            numRetries++;
        }

        // Detect no enabled Contacts
        if (enabledContacts.isEmpty()) {
            Log.e(TAG, "getRandomEnabledContact: no enabled Contacts found in database after " + MAX_DB_RETRIES + " queries. Returning null.");
            return null;
        }

        // Return a random Contact from the list of enabled Contacts
        Random randy = new Random();
        int index = randy.nextInt(enabledContacts.size());
        return enabledContacts.get(index);
    }

    /**
     * Returns a randomly chosen Message with the given @contactId from the database, or null if none were found.
     *
     * @param context
     * @param contactId
     * @return
     */
    private Message getRandomMessageForContact(Context context, int contactId) {
        // Get all Messages with the given contactId
        MessageRepository messageRepository = new MessageRepository(context);
        List<Message> messages = messageRepository.getMessagesForContactSync(contactId);

        // First accesses return empty workaround
        int numRetries = 0;
        while (messages.isEmpty() && numRetries < MAX_DB_RETRIES) {
            messages = messageRepository.getMessagesForContactSync(contactId);
            numRetries++;
        }

        // Detect no Messages found with given contactId
        if (messages.isEmpty()) {
            Log.e(TAG, "getRandomMessageForContact: no Messages with contactId " + contactId + " found in database after " + MAX_DB_RETRIES + " queries. Returning null.");
            return null;
        }

        // Return a random Message from the list of Messages
        Random randy = new Random();
        int index = randy.nextInt(messages.size());
        return messages.get(index);
    }

    /**
     * Returns a Notification to alert the user of no enabled Contacts.
     *
     * @param context
     * @return
     */
    private Notification getNoEnabledContactsNotification(Context context) {
        // create and return the notification
        // TODO update small icon
        //  use resource Strings
        //  notification click -> ContactsFragment
        return new NotificationCompat.Builder(context, App.CHANNEL_ID_ALERTS)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("No enabled contacts")
                .setContentText("Ross Quotes is scheduling messages, but you don't have any enabled contacts.")
                // set stuff here similar to our notification channel in App.java, to support APIs lower than O
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }

    /**
     * Returns a Notification to alert the user of no Messages found for given @contact.
     *
     * @param context
     * @param contact
     * @return
     */
    private Notification getNoMessagesForContactNotification(Context context, Contact contact) {
        // create and return the notification
        // TODO update small icon
        //  use resource Strings
        //  notification click -> ContactDetailsFragment
        return new NotificationCompat.Builder(context, App.CHANNEL_ID_ALERTS)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("No messages found")
                .setContentText("You should be seeing a message from " + contact.getName() + ", but we couldn't find any.")
                // set stuff here similar to our notification channel in App.java, to support APIs lower than O
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }

    /**
     * Returns a Notification to display the @contact's @message.
     *
     * @param context
     * @param contact
     * @param message
     * @return
     */
    private Notification getContactMessageNotification(Context context, Contact contact, Message message) {
        // Get Bitmap from Contact's image Uri to be used for notification's large icon
        //  based on this SO answer: https://stackoverflow.com/a/4717740
        Uri imgUri = Uri.parse(contact.getImageAbsolutePath());
        Bitmap largeIcon = null;
        try {
            largeIcon = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imgUri);
        } catch (Exception e) {
            Log.e(TAG, "getContactMessageNotification: ", e);
        }

        // create and return the notification
        return new NotificationCompat.Builder(context, App.CHANNEL_ID_MESSAGES)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle(contact.getName())
                .setContentText(message.getText())
                .setLargeIcon(largeIcon)
                // set stuff here similar to our notification channel in App.java, to support APIs lower than O
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
    }

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
