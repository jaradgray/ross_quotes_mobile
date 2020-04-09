package com.jgendeavors.rossquotes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PremiumDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create and return the Dialog to be displayed
        //  we'll make a custom AlertDialog

        // inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_premium, null);

        // build Dialog with Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(R.string.dialog_premium_title)
                .setNegativeButton(R.string.dialog_premium_button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // cancel dialog
                        PremiumDialog.this.getDialog().cancel();
                    }
                })
                .setPositiveButton(R.string.dialog_premium_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO notify listener of positive button click
                    }
                });

        return builder.create();
    }
}
