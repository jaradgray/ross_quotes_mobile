package com.jgendeavors.rossquotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

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
        super(context, attrs, defStyleAttr);
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

    /** Set value of instance variable based on persisted preference */
    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setInterval(getPersistedString((String)defaultValue));
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_interval;
    }


    // Getter & Setter
    public String getInterval() { return mInterval; }

    public void setInterval(String value) {
        mInterval = value;
        persistString(value); // update SharedPreferences
    }
}
