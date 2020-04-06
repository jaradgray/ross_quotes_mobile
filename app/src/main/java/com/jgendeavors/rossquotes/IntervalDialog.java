package com.jgendeavors.rossquotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

/**
 * The IntervalDialog class handles the UI interaction aspect of an IntervalDialogPreference.
 *
 * Based on the guide here: https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec
 */
public class IntervalDialog extends PreferenceDialogFragmentCompat {

    // Instance variables
    private Spinner mValueSpinner;
    private Spinner mUnitSpinner;


    /**
     * Our app will retrieve an IntervalDialog instance with this static method.
     * The returned instance is associated with the preference
     * (i.e. SharedPreference key-value pair) with the given @prefKey.
     *
     * @param prefKey
     * @return
     */
    public static IntervalDialog getInstance(String prefKey) {
        final IntervalDialog fragment = new IntervalDialog();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, prefKey);
        fragment.setArguments(bundle);

        return fragment;
    }


    // Overridden methods


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Set widget state to match persisted data from SharedPreferences
        //  i.e. "display the persisted interval data when this Dialog is shown"

        // get references to widgets
        mValueSpinner = view.findViewById(R.id.pref_dialog_interval_spinner_value);
        mUnitSpinner = view.findViewById(R.id.pref_dialog_interval_spinner_unit);

        // TODO do I need to implement this?
        mValueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // get the value from the related preference
        String intervalString = null;
        DialogPreference preference = getPreference();
        if (preference instanceof IntervalDialogPreference) {
            intervalString = ((IntervalDialogPreference) preference).getInterval();
        }

        if (intervalString == null) return;

        // split the String from SharedPrefs into Strings for VALUE and UNIT
        String[] intervalSplit = intervalString.split(",");
        String value = intervalSplit[0];
        String unit = intervalSplit[1];

        // set mValueSpinner's selection to index of the persisted VALUE entry
        String[] arrValues = getResources().getStringArray(R.array.pref_interval_values);
        List<String> listValues = new ArrayList<>(Arrays.asList(arrValues));
        int index = listValues.indexOf(value);
        mValueSpinner.setSelection(index);

        // set mUnitSpinner's selection to index of the persisted UNIT entry
        String[] arrUnits = getResources().getStringArray(R.array.pref_entries_interval_units);
        List<String> listUnits = new ArrayList<>(Arrays.asList(arrUnits));
        index = listUnits.indexOf(unit);
        mUnitSpinner.setSelection(index);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Generate value to persist based on widget state
            String value = mValueSpinner.getSelectedItem().toString();
            String unit = mUnitSpinner.getSelectedItem().toString();
            String intervalString = value + "," + unit;

            // Get the related Preference and save the value
            DialogPreference preference = getPreference();
            if (preference instanceof IntervalDialogPreference) {
                IntervalDialogPreference intervalDialogPref = (IntervalDialogPreference) preference;

                // Validate interval value based on pref key

                if (intervalDialogPref.getKey().equals(App.PREF_KEY_MIN_INTERVAL)) {
                    // interval represented by intervalString can't be larger than current max interval
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String maxIntervalString = prefs.getString(App.PREF_KEY_MAX_INTERVAL, null);
                    if (getIntervalMillis(intervalString) > getIntervalMillis(maxIntervalString)) {
                        // TODO use resource String
                        Toast.makeText(getContext(), "Interval too large", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (intervalDialogPref.getKey().equals(App.PREF_KEY_MAX_INTERVAL)) {
                    // interval represented by intervalString can't be smaller than current min interval
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String minIntervalString = prefs.getString(App.PREF_KEY_MIN_INTERVAL, null);
                    if (getIntervalMillis(intervalString) < getIntervalMillis(minIntervalString)) {
                        // TODO use resource String
                        Toast.makeText(getContext(), "Interval too small", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // The following conditional allows the client to ignore the user value (e.g. if it's the same as the currently-persisted value)
                if (intervalDialogPref.callChangeListener(intervalString)) {
                    // Save the value
                    intervalDialogPref.setInterval(intervalString);
                }
            }
        }
    }


    // Private methods

    /**
     * Returns the number of millis represented by the given @interval
     *
     * @param interval
     * @return
     */
    public static int getIntervalMillis(String interval) {
        String[] split = interval.split(",");
        int numUnits = Integer.parseInt(split[0]);
        String unit = split[1];

        final int MILLIS_PER_SECOND = 1000;
        final int MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;
        final int MILLIS_PER_HOUR   = MILLIS_PER_MINUTE * 60;
        final int MILLIS_PER_DAY    = MILLIS_PER_HOUR * 24;

        int millisPerUnit = 0;
        switch (unit) {
            case "Seconds":
                millisPerUnit = MILLIS_PER_SECOND;
                break;
            case "Minutes":
                millisPerUnit = MILLIS_PER_MINUTE;
                break;
            case "Hours":
                millisPerUnit = MILLIS_PER_HOUR;
                break;
            case "Days":
                millisPerUnit = MILLIS_PER_DAY;
                break;
        }

        return numUnits * millisPerUnit;
    }
}
