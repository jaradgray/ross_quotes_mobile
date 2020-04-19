package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * AndroidX's Preference library handles a lot of the binding of View to SharedPreferences values,
 * but we want a bit of custom behavior, i.e. we want to enable/disable the schedule category preferences
 * based on the global app enabled/disabled preference. We use this ViewModel class for that.
 */
public class SettingsFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private MutableLiveData<Boolean> mIsAppEnabled = new MutableLiveData<>(); // connected to the PREF_KEY_APP_ENABLED preference
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener; // declare the listener as a member so it doesn't get GC'd


    // Constructor
    public SettingsFragmentViewModel(@NonNull Application application) {
        super(application);

        // Initialize member based on persisted pref
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        boolean isEnabled = prefs.getBoolean(App.PREF_KEY_APP_ENABLED, true);
        mIsAppEnabled.postValue(isEnabled);

        // Update member on pref updated
        // declare listener
        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefKey) {
                if (prefKey.equals(App.PREF_KEY_APP_ENABLED)) {
                    // update member
                    boolean isEnabled = sharedPreferences.getBoolean(prefKey, true);
                    mIsAppEnabled.postValue(isEnabled);
                }
            }
        };
        // register listener
        prefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
    }


    // API methods

    public LiveData<Boolean> isAppEnabled() { return mIsAppEnabled; }
}
