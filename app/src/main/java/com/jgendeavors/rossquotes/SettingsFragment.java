package com.jgendeavors.rossquotes;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);

        // Set summaries of our custom IntervalDialogPreferences to reflect persisted values

        IntervalDialogPreference minIntervalPref = findPreference("PREF_KEY_MIN_INTERVAL");

        if (minIntervalPref != null) {
            String summary = minIntervalPref.getInterval().replace(',', ' ');
            minIntervalPref.setSummary(summary);
        }
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
