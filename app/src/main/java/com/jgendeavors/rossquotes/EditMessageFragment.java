package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class EditMessageFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_MESSAGE_ID = "ARG_KEY_MESSAGE_ID";
    public static final int ARG_VALUE_NO_MESSAGE_ID = -1;
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";


    // Instance variables
    private EditMessageFragmentViewModel mViewModel;
    private EditText mEtMessage;


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
        mEtMessage = view.findViewById(R.id.fragment_edit_message_et_message);

        // Give the EditText an OnFocusChangeListener
        //  we'll use this to show/hide the soft keyboard
        mEtMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Util.showKeyboardTo(getContext(), view);
                } else {
                    Util.hideKeyboardFrom(getContext(), view);
                }
            }
        });

        // Show the soft keyboard by requesting focus on the EditText
        mEtMessage.requestFocus();

        // Get the Message's id from args
        // Note: this will be ARG_VALUE_NO_MESSAGE_ID when we're creating a new Message, i.e. editing a Message that isn't stored in the database yet
        final int messageId = getArguments().getInt(ARG_KEY_MESSAGE_ID);

        // Request a ViewModel from the Android system
        mViewModel = ViewModelProviders.of(this).get(EditMessageFragmentViewModel.class);

        // Set ViewModel's Message if we're dealing with an existing Message
        if (messageId != ARG_VALUE_NO_MESSAGE_ID) {
            mViewModel.setMessageById(messageId);
        }

        // Observe ViewModel's LiveData
        mViewModel.getMessage().observe(getViewLifecycleOwner(), new Observer<Message>() {
            @Override
            public void onChanged(Message message) {
                // update etMessage's text
                mEtMessage.setText(message.getText());
            }
        });
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
                // Save message
                int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID); // get Contact id from args
                mViewModel.saveMessage(contactId, mEtMessage.getText().toString());
                // TODO hide soft keyboard
                // Navigate back/up
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                return true;
            case R.id.menu_item_delete_message:
                // Delete message
                mViewModel.deleteMessage();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                // TODO hide soft keyboard
                // Navigate back/up
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
