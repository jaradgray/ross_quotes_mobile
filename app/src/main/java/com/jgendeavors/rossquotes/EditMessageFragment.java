package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class EditMessageFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_MESSAGE_ID = "ARG_KEY_MESSAGE_ID";


    // Overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Indicate we'll have an options menu
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        EditText etMessage = view.findViewById(R.id.fragment_edit_message_et_message);

        // Get the Message's id from args
        // TODO this will be -1 when we're creating a new Message, i.e. editing a Message that isn't stored in the database yet
        int messageId = getArguments().getInt(ARG_KEY_MESSAGE_ID, -1);

        // Request a ViewModel from the Android system
        EditMessageFragmentViewModel viewModel = ViewModelProviders.of(this).get(EditMessageFragmentViewModel.class);

        // Set etMessage's text based on ViewModel's Message
        Message message = viewModel.getMessage(messageId);
        etMessage.setText(message.getText());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_edit_message_options_menu, menu); // inflate the Fragment's options menu
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_message:
                // TODO save message
                Toast.makeText(getContext(), "TODO save message", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_delete_message:
                // TODO delete message
                Toast.makeText(getContext(), "TODO delete message", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
