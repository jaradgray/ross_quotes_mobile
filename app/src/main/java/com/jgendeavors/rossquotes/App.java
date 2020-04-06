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
    // Pref default values
    public static final String PREF_DEFAULT_VALUE_MIN_INTERVAL = "5,Seconds";
    public static final String PREF_DEFAULT_VALUE_MAX_INTERVAL = "7,Hours";


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

        // Handle changes to SharedPreferences
        // declare the listener
        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefKey) {
                switch (prefKey) {
                    case PREF_KEY_APP_ENABLED:
                        // Cancel or set the alarm
                        boolean isEnabled = sharedPreferences.getBoolean(prefKey, true);
                        if (!isEnabled) {
                            // app disabled, cancel alarm if it's set
                            if (AlarmHelper.isSet(getApplicationContext())) {
                                AlarmHelper.cancelAlarm(getApplicationContext());
                            }
                            break;
                        }
                        // if pref was set to enabled, execution will proceed to next cases
                    case PREF_KEY_MIN_INTERVAL:
                    case PREF_KEY_MAX_INTERVAL:
                        // Set alarm to a random time between the persisted min and max intervals from now
                        // get the persisted interval values (Strings)
                        String minIntervalString = sharedPreferences.getString(PREF_KEY_MIN_INTERVAL, PREF_DEFAULT_VALUE_MIN_INTERVAL);
                        String maxIntervalString = sharedPreferences.getString(PREF_KEY_MAX_INTERVAL, PREF_DEFAULT_VALUE_MAX_INTERVAL);
                        // convert persisted Strings to millis
                        int minInterval = IntervalDialog.getIntervalMillis(minIntervalString);
                        int maxInterval = IntervalDialog.getIntervalMillis(maxIntervalString);
                        // set alarm
                        AlarmHelper.setAlarm(getApplicationContext(), minInterval, maxInterval);
                        break;
                }
            }
        };
        // register the listener
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.registerOnSharedPreferenceChangeListener(mPrefsListener);

        // Initialize preferences to default values if they don't exist
        //  this is to set initial preference values on fresh app install
        if (!prefs.contains(PREF_KEY_APP_ENABLED)) {
            prefs.edit().putBoolean(PREF_KEY_APP_ENABLED, true).apply();
        }
        if (!prefs.contains(PREF_KEY_MIN_INTERVAL)) {
            prefs.edit().putString(PREF_KEY_MIN_INTERVAL, PREF_DEFAULT_VALUE_MIN_INTERVAL).apply();
        }
        if (!prefs.contains(PREF_KEY_MAX_INTERVAL)) {
            prefs.edit().putString(PREF_KEY_MAX_INTERVAL, PREF_DEFAULT_VALUE_MAX_INTERVAL).apply();
        }
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
