package com.jgendeavors.rossquotes;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * The Util class provides handy static utility methods for use by any app component.
 */
public class Util {
    /**
     * Hides the soft keyboard.
     * Based on this SO answer: https://stackoverflow.com/a/17789187
     * @param context
     * @param view
     */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard.
     * Reverse-engineered from the above method
     *
     * @param context
     * @param view
     */
    public static void showKeyboardTo(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }
}
