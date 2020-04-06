package com.jgendeavors.rossquotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

/**
 * The IntervalDialogPreference class handles the data management (i.e. preference persisting) aspect of an IntervalDialogPreference.
 *
 * Based on the guide here: https://medium.com/@JakobUlbrich/building-a-settings-screen-for-android-part-3-ae9793fd31ec
 */
public class IntervalDialogPreference extends DialogPreference {
    // Instance variables
    private String mInterval;


    // Constructors

    public IntervalDialogPreference(Context context) {
        this(context, null);
    }

    public IntervalDialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public IntervalDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public IntervalDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Do custom stuff here
        // ...
        // read attributes etc.
    }


    // Overridden methods

    /** Returns the default value set from XML attribute */
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // Default value from attribute
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        // Set value of instance variable based on persisted preference
        setInterval(getPersistedString((String)defaultValue));
    }

    @Override
    public int getDialogLayoutResource() {
        // Return the layout resource for this DialogPreference's associated DialogFragment
        return R.layout.pref_dialog_interval;
    }


    // Getter & Setter
    public String getInterval() { return mInterval; }

    public void setInterval(String value) {
        mInterval = value;
        persistString(value); // update SharedPreferences
        setSummary(value.replace(',', ' ')); // update summary
    }
}
