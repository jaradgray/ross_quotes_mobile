package com.jgendeavors.rossquotes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class EditContactFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";
    public static final int ARG_VALUE_NO_CONTACT_ID = -1;
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 1;


    // Instance variables
    private ImageView mIvContactPhoto;
    private EditText mEtName;
    private EditContactFragmentViewModel mViewModel;


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
        mIvContactPhoto = view.findViewById(R.id.fragment_edit_contact_iv_contact_photo);
        mEtName = view.findViewById(R.id.fragment_edit_contact_et_name);
        Button bSave = view.findViewById(R.id.fragment_edit_contact_b_save);

        // Request a ViewModel from the Android system
        mViewModel = ViewModelProviders.of(this).get(EditContactFragmentViewModel.class);

        // Observe ViewModel's LiveData if we're dealing with an existing Contact
        final int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);
        if (contactId != ARG_VALUE_NO_CONTACT_ID) {
            mViewModel.getContact(contactId).observe(getViewLifecycleOwner(), new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    // Update UI based on contact
                    // contact photo
                    String imgPath = contact.getImageAbsolutePath();
                    Uri imgUri = Uri.parse(imgPath);
                    mIvContactPhoto.setImageURI(imgUri);
                    mIvContactPhoto.setTag(imgPath); // store image's path in iv's tag
                    // name
                    mEtName.setText(contact.getName());
                }
            });
        }

        // Handle clicks on contact photo
        mIvContactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Select contact photo
                // based on this SO answer: https://stackoverflow.com/a/5309217
                Intent pickImageIntent = new Intent();
                pickImageIntent.setType("image/*");
                pickImageIntent.setAction(Intent.ACTION_PICK);
                // verify that the Intent will resolve to an Activity; i.e. make sure the device has an app that can handle the Intent
                if (pickImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(pickImageIntent, "Select Image"), REQUEST_CODE_CHOOSE_IMAGE);
                }
            }
        });

        // Handle clicks on save button
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert/update Contact
                mViewModel.insertOrUpdateContact(contactId, mEtName.getText().toString(), mIvContactPhoto.getTag().toString());
                // TODO hide soft keyboard
                // Navigate up/back
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // User selected a contact image
        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // Save contact
            final int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);
            String imgPath = data.getDataString();
            mViewModel.insertOrUpdateContact(contactId, mEtName.getText().toString(), imgPath);
        }
    }
}
