package com.jgendeavors.rossquotes;

import android.os.Bundle;
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
 */
public class IntervalDialog extends PreferenceDialogFragmentCompat {

    // Instance variables
    private Spinner mValueSpinner;
    private Spinner mUnitSpinner;


    public static IntervalDialog getInstance(String key) {
        final IntervalDialog fragment = new IntervalDialog();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;
    }


    // Overridden methods


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        // Set widget data to match persisted data from SharedPreferences

        mValueSpinner = view.findViewById(R.id.pref_dialog_interval_spinner_value);
        mUnitSpinner = view.findViewById(R.id.pref_dialog_interval_spinner_unit);

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

        // Set spinners based on preference value

        // split the interval String into value and unit
        String[] intervalSplit = intervalString.split(",");
        String value = intervalSplit[0];
        String unit = intervalSplit[1];

        // set mValueSpinner's selection to index of its selected entry
        String[] arrValues = getResources().getStringArray(R.array.pref_interval_values);
        List<String> listValues = new ArrayList<>(Arrays.asList(arrValues));
        int index = listValues.indexOf(value);
        mValueSpinner.setSelection(index);

        // set mUnitSpinner's selection to index of its selected entry
        String[] arrUnits = getResources().getStringArray(R.array.pref_entries_interval_units);
        List<String> listUnits = new ArrayList<>(Arrays.asList(arrUnits));
        index = listUnits.indexOf(unit);
        mUnitSpinner.setSelection(index);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = mValueSpinner.getSelectedItem().toString();
            String unit = mUnitSpinner.getSelectedItem().toString();
            Toast.makeText(getContext(), "value: " + value + ", unit: " + unit, Toast.LENGTH_SHORT).show();
            // TODO save interval based on state of spinners
        }
    }
}
