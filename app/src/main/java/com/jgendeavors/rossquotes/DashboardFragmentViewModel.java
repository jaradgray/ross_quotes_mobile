package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DashboardFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private ReceivedMessageRepository mReceivedMessageRepository;
    private ContactRepository mContactRepository;

    private LiveData<List<ReceivedMessage>> mAllReceivedMessages;
    private LiveData<List<Contact>> mAllContacts;
    private MutableLiveData<Boolean> mIsAppEnabled = new MutableLiveData<>();

    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;


    // Constructor
    public DashboardFragmentViewModel(@NonNull Application application) {
        super(application);

        // Set instance variables
        mReceivedMessageRepository = new ReceivedMessageRepository(application);
        mContactRepository = new ContactRepository(application);

        // set database-dependent members
        mAllReceivedMessages = mReceivedMessageRepository.getAllReceivedMessages();
        mAllContacts = mContactRepository.getAlphabetizedContacts();

        // set and update mIsAppEnabled. It's tied to the PREF_KEY_APP_ENABLED preference
        // initialize mIsAppEnabled based on pref
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(application);
        boolean isEnabled = prefs.getBoolean(App.PREF_KEY_APP_ENABLED, true);
        mIsAppEnabled.postValue(isEnabled);

        // update mIsAppEnabled on pref changed
        // declare the listener
        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefKey) {
                // if the changed pref was the app enabled preference...
                if (prefKey.equals(App.PREF_KEY_APP_ENABLED)) {
                    // update member
                    boolean isEnabled = sharedPreferences.getBoolean(prefKey, true);
                    mIsAppEnabled.postValue(isEnabled);
                }
            }
        };
        // register the listener
        prefs.registerOnSharedPreferenceChangeListener(mPrefListener);
    }


    // API methods
    public LiveData<List<ReceivedMessage>> getAllReceivedMessages() { return mAllReceivedMessages; }
    public LiveData<List<Contact>> getAllContacts() { return mAllContacts; }
    public LiveData<Boolean> getIsAppEnabled() { return mIsAppEnabled; }
}
