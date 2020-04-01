package com.jgendeavors.rossquotes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * The App class wraps our application and we can setup code in here
 * that runs once when the application starts, such as creating
 * notification channels/
 *
 * Remember to register this class in the manifest.
 */
public class App extends Application {
    // Constants
    public static final String CHANNEL_ID_MESSAGES = "CHANNEL_ID_MESSAGES";


    // Lifecycle overrides

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO delete this; just for testing
        deleteDatabase(QuotesRoomDatabase.DB_NAME);

        createNotificationChannels();
    }


    // Private methods

    /**
     * Creates the notification channels the app will use to show notifications to the user.
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Set up channel for displaying message/quote notifications
            NotificationChannel messagesChannel = new NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    "Messages" /* channel name, visible to user */,
                    NotificationManager.IMPORTANCE_HIGH
            );

            // TODO replace this and previous name string with resource strings
            messagesChannel.setDescription("This notification channel notifies the user of new messages.");

            // Register the channels so we can send notifications through them
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(messagesChannel);
        }
    }
}
