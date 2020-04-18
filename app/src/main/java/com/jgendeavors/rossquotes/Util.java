package com.jgendeavors.rossquotes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.core.content.ContextCompat;

/**
 * The Util class provides handy static utility methods for use by any app component.
 */
public class Util {
    // Constants
    private static final String TAG = "Util";


    // Methods

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

    /**
     * Writes @bitmap's data to a File named @fileName in the pictures directory in app-specific external storage,
     * and returns the written File's absolute path.
     *
     * @param context
     * @param bitmap
     * @param fileName
     * @return
     */
    public static String saveToExternalStorage(Context context, Bitmap bitmap, String fileName) {
        // Verify storage is available
        String storageState = Environment.getExternalStorageState();
        if (!storageState.equals(Environment.MEDIA_MOUNTED) && !storageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return null;
        }

        // Get the app-specific primary external storage directory for pictures
        File externalPicutresDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the File we'll write the Bitmap data to
        File bitmapFile = new File(externalPicutresDir, fileName);

        // Write Bitmap data to File
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(bitmapFile);
            // use Bitmap.compress() to write the Bitmap to the output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.close();
        } catch (IOException e) {
            // Unable to create File
            Log.e(TAG, "saveToExternalStorage: Error writing " + bitmapFile, e);

            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Log.e(TAG, "saveToExternalStorage: Error closing " + out, ex);
                }
            }
        }

        // Return written File's path
        return bitmapFile.getAbsolutePath();
    }
}
