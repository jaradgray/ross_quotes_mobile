package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class EditContactFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";
    public static final int ARG_VALUE_NO_CONTACT_ID = -1;


    // Lifecycle overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        ImageView ivContactPhoto = view.findViewById(R.id.fragment_edit_contact_iv_contact_photo);
        final EditText etName = view.findViewById(R.id.fragment_edit_contact_et_name);
        Button bSave = view.findViewById(R.id.fragment_edit_contact_b_save);

        // Request a ViewModel from the Android system
        EditContactFragmentViewModel viewModel = ViewModelProviders.of(this).get(EditContactFragmentViewModel.class);

        // Observe ViewModel's LiveData if we're dealing with an existing Contact
        int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);
        if (contactId != ARG_VALUE_NO_CONTACT_ID) {
            viewModel.getContact(contactId).observe(getViewLifecycleOwner(), new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    // update UI based on contact
                    // TODO update contact photo
                    etName.setText(contact.getName());
                }
            });
        }
    }
}
