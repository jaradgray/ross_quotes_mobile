package com.jgendeavors.rossquotes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PremiumDialog extends DialogFragment {

    // Instance variables
    private IPurchaseActionListener mPurchaseActionListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create and return the Dialog to be displayed
        //  we'll make a custom AlertDialog

        // inflate the custom layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
                        // Notify listener of positive button click (i.e. purchase action taken)
                        mPurchaseActionListener.onPurchaseAction(MainActivity.PRODUCT_ID_PREMIUM);
                    }
                });

        return builder.create();
    }

    /**
     * We use this method to enforce this Fragment's host Activity must implement the IPurchaseActionListener interface.
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Enforce host Activity implements IPurchaseActionListener interface
        try {
            mPurchaseActionListener = (IPurchaseActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + IPurchaseActionListener.class.toString());
        }
    }
}
