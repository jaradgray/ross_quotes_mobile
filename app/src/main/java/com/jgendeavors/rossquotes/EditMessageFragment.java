package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class EditMessageFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_MESSAGE_ID = "ARG_KEY_MESSAGE_ID";


    // Lifecycle overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        TextView tvMessage = view.findViewById(R.id.fragment_edit_message_tv_message);

//        // Get the Message's id from args
//        // TODO this will be -1 when we're creating a new Message, i.e. editing a Message that isn't stored in the database yet
//        int messageId = getArguments().getInt(ARG_KEY_MESSAGE_ID, -1);
//
//        // Request a ViewModel from the Android system
//        EditMessageFragmentViewModel viewModel = ViewModelProviders.of(this).get(EditMessageFragmentViewModel.class);
//
//        // Set tvMessage's text based on ViewModel's Message
//        Message message = viewModel.getMessage(messageId);
//        tvMessage.setText(message.getText());
    }
}
