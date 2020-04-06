package com.jgendeavors.rossquotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    // Instance variables
    SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;


    // Overridden methods

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This is a safe place to do ViewModel stuff
        // We want to link the Schedule Category's enabled/disabled state to the persisted global app enabled/disabled preference
        //  we can do that through the ViewModel

        // Get a reference to the schedule category so we can set its data later
        final Preference scheduleCategory = findPreference("schedule_category");

        // Request a ViewModel from the Android system
        SettingsFragmentViewModel viewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel.class);

        // Observe the ViewModel's LiveData
        viewModel.isAppEnabled().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAppEnabled) {
                // enable/disable schedule category based on global app enable/disable
                if (scheduleCategory != null) {
                    scheduleCategory.setEnabled(isAppEnabled);
                }
            }
        });
    }

    /**
     * Must be overridden to show custom DialogPreferences' DialogFragments
     *
     * Based on the guide here: https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec
     *
     * @param preference
     */
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Show the DialogFragment associated with the given Preference
        if (preference instanceof IntervalDialogPreference) {
            IntervalDialog dialog = IntervalDialog.getInstance(preference.getKey());
            dialog.setTargetFragment(this, 0);
            dialog.show(getParentFragmentManager(), "blahhhhh");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
