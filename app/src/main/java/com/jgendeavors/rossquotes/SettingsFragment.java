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

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {

        if (preference instanceof IntervalDialogPreference) {
            IntervalDialog dialog = IntervalDialog.getInstance(preference.getKey());
            dialog.setTargetFragment(this, 0);
            dialog.show(getParentFragmentManager(), "blahhhhh");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
