package com.jgendeavors.rossquotes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class EditContactFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";
    public static final int ARG_VALUE_NO_CONTACT_ID = -1;
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 1;
    // permission request
    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;


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

        // Give the EditText an OnFocusChangeListener
        //  we'll use this to show/hide the soft keyboard
        mEtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Util.showKeyboardTo(getContext(), view);
                } else {
                    Util.hideKeyboardFrom(getContext(), view);
                }
            }
        });

        // Request a ViewModel from the Android system
        mViewModel = ViewModelProviders.of(this).get(EditContactFragmentViewModel.class);

        // Perform initialization based on if we're dealing with a new or existing Contact
        final int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);
        if (contactId == ARG_VALUE_NO_CONTACT_ID) {
            // New Contact
            // Set ImageView's image to default values
            final String resPath = "android.resource://com.jgendeavors.rossquotes/";
            String imgPath = resPath + R.drawable.ic_default_avatar;
            Uri imgUri = Uri.parse(imgPath);
            mIvContactPhoto.setImageURI(imgUri);
        } else {
            // Existing Contact
            // Observe ViewModel's LiveData
            mViewModel.getContact(contactId).observe(getViewLifecycleOwner(), new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    // Update UI based on contact
                    // contact photo
                    String imgPath = contact.getImageAbsolutePath();
                    Uri imgUri = Uri.parse(imgPath);
                    mIvContactPhoto.setImageURI(imgUri);
                    // name
                    mEtName.setText(contact.getName());
                }
            });
        }

        // Handle clicks on contact photo
        mIvContactPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if we have permission to read external storage
                // TODO This might not be the only place we need to check for this permission
                //  The app was crashing on clicking the "no messages for contact" notification because of this permission denial.
                //  We're requesting it here because this is the first place in the UX we need it, but technically the user could
                //  turn off this permission at any time, so we really should check if we need to request it any time we access a URI
                //  that could be on the device's external storage...
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission isn't granted, request permission via prompt
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                    // User response is processed by the onRequestPermissionsResult() callback
                } else {
                    // Permission has already been granted

                    // Select contact photo
                    selectContactImage();
                }
            }
        });

        // Handle clicks on save button
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert/update Contact
                mViewModel.insertOrUpdateContact(contactId, mEtName.getText().toString());
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
            // set ViewModel's selectedImageUri member
            mViewModel.setSelectedImage(data.getData());

            // update ImageView
            mIvContactPhoto.setImageURI(data.getData());

            // TODO delete
            // Update ImageView's image and tag based on selected image
//            mIvContactPhoto.setImageURI(data.getData());
//            mIvContactPhoto.setTag(data.getDataString());
        }
    }

    /**
     * Callback method for handling user's response to permission requests
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // We request this permission on contact image click, so now we can let the user select a contact image
                    selectContactImage();
                } else {
                    // Permission denied
                    // TODO handle permission denied if anything ever needs handling (nothing to do now)
                }
                break;
        }
    }


    // Private methods

    /**
     * Allow the user to pick an image from their device, via any app that can pick images.
     *
     * based on this SO answer: https://stackoverflow.com/a/5309217
     */
    private void selectContactImage() {
        Intent pickImageIntent = new Intent();
        pickImageIntent.setType("image/*");
        pickImageIntent.setAction(Intent.ACTION_PICK);
        // verify that the Intent will resolve to an Activity; i.e. make sure the device has an app that can handle the Intent
        if (pickImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(pickImageIntent, "Select Image"), REQUEST_CODE_CHOOSE_IMAGE);
        }
    }
}
