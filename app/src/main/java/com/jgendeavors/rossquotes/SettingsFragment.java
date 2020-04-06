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
            minIntervalPref.setSummaryProvider(new Preference.SummaryProvider() {
                @Override
                public CharSequence provideSummary(Preference preference) {
                    String result = null;
                    if (preference instanceof IntervalDialogPreference) {
                        // get persisted interval
                        IntervalDialogPreference pref = (IntervalDialogPreference) preference;
                        String interval = pref.getInterval();
                        // format interval string
                        result = interval.replace(',', ' ');
                    }
                    return result;
                }
            });
        }
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
