package com.jgendeavors.rossquotes;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);
    }

    /**
     * Must be overridden to show custom DialogPreferences' DialogFragments
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
