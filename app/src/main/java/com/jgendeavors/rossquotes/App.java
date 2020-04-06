package com.jgendeavors.rossquotes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

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
    // Pref keys
    public static final String PREF_KEY_APP_ENABLED = "PREF_KEY_APP_ENABLED";
    public static final String PREF_KEY_MIN_INTERVAL = "PREF_KEY_MIN_INTERVAL";
    public static final String PREF_KEY_MAX_INTERVAL = "PREF_KEY_MAX_INTERVAL";


    // Instance variables
    SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener; // just to be safe, so it doesn't get GC'd (which might happen if it were declared as an anonymous inner class)


    // Lifecycle overrides

    @Override
    public void onCreate() {
        super.onCreate();

        // Put code that should be performed once on app launch here

        // TODO delete this; just for testing
        deleteDatabase(QuotesRoomDatabase.DB_NAME);

        createNotificationChannels();

        // Set the alarm to start sending notifications if it isn't already set
        if (!AlarmHelper.isSet(getApplicationContext())) {
            // Set alarm to 5 seconds from now
            // TODO set alarm to a random time within set timeframe
            Calendar c = Calendar.getInstance();
            c.add(Calendar.SECOND, 5);
            AlarmHelper.setAlarm(getApplicationContext(), c);
        }

        // Handle changes to SharedPreferences
        // declare the listener
        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefKey) {
                switch (prefKey) {
                    case App.PREF_KEY_APP_ENABLED:
                        // TODO implement this
                        Toast.makeText(getApplicationContext(), "App: " + App.PREF_KEY_APP_ENABLED + " changed.", Toast.LENGTH_SHORT).show();
                        break;
                    case App.PREF_KEY_MIN_INTERVAL:
                        // TODO implement this
                        Toast.makeText(getApplicationContext(), "App: " + App.PREF_KEY_MIN_INTERVAL + " changed.", Toast.LENGTH_SHORT).show();
                        break;
                    case App.PREF_KEY_MAX_INTERVAL:
                        // TODO implement this
                        Toast.makeText(getApplicationContext(), "App: " + App.PREF_KEY_MAX_INTERVAL + " changed.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        // register the listener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
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
