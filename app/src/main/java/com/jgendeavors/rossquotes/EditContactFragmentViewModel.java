package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EditContactFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private ContactRepository mRepository;
    private Bitmap mSelectedImage;


    // Constructor
    public EditContactFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ContactRepository(application);
    }


    // API methods

    public LiveData<Contact> getContact(int contactId) { return mRepository.getContact(contactId); }

    /**
     * Inserts or updates a Contact in the database, via the Repository
     * @param contactId
     * @param name
     */
    public void insertOrUpdateContact(int contactId, String name) {
        Contact contact;
        if (contactId == EditContactFragment.ARG_VALUE_NO_CONTACT_ID) {
            // Insert new Contact
            // TODO maybe validate by making sure there's a name or picture
            int id = getNextContactId();
            // TODO save contact's image to internal/external storage here
            // get a path for the contact's image, if it's been set
            String imageAbsolutePath = null;
            if (mSelectedImage != null) {
                // TODO save image, get its path
                imageAbsolutePath = Util.saveToExternalStorage(getApplication(), mSelectedImage, "" + id);
            }
            contact = new Contact(id, name, imageAbsolutePath, true);
            mRepository.insert(contact);
        } else {
            // Update existing Contact
            Contact oldContact = mRepository.getContactSync(contactId);
            // image path
            String imageAbsolutePath = oldContact.getImageAbsolutePath();
            // if the in-memory image has been set, we need to save it to our app's storage and use the saved image's path
            if (mSelectedImage != null) {
                // TODO save image, get its path
                imageAbsolutePath = Util.saveToExternalStorage(getApplication(), mSelectedImage, "" + contactId);
            }
            // get Contact's isEnabled value
            boolean isEnabled = oldContact.getIsEnabled();
            // update contact
            contact = new Contact(contactId, name, imageAbsolutePath, isEnabled);
            mRepository.update(contact);
        }
    }

    /**
     * Creates an in-memory Bitmap of the image at @uri.
     * Call this when the user selects an image in the EditContactFragment.
     *
     * @param uri
     */
    public void setSelectedImage(Uri uri) {
        // TODO create a Bitmap from uri
        try {
            mSelectedImage = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), uri);
//            mSelectedImage = Bitmap.createScaledBitmap(mSelectedImage, 512, 512, true);
        } catch (Exception e) {
            Log.e("EditContactFragmentVM", "setSelectedImage: Error: ", e);
        }
    }


    // Private methods

    /**
     * Returns a unique integer to be used for Contact IDs.
     * Based on this SO answer: https://stackoverflow.com/a/41122403
     *
     * @return
     */
    private int getNextContactId() {
        // get the last ID to be stored in SharedPreferences and increment it
        final String PREF_LAST_CONTACT_ID = "PREF_LAST_CONTACT_ID";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        int result = prefs.getInt(PREF_LAST_CONTACT_ID, Contact.FIRST_FREE_CONTACT_ID) + 1;
        if (result == Integer.MAX_VALUE) result = Contact.FIRST_FREE_CONTACT_ID; // just to be sure!
        // update SharedPreferences
        prefs.edit().putInt(PREF_LAST_CONTACT_ID, result).apply();

        return result;
    }
}
