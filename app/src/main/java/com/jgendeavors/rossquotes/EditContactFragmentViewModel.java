package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EditContactFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private ContactRepository mRepository;


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
    public void insertOrUpdateContact(int contactId, String name, String imageAbsolutePath) {
        Contact contact;
        if (contactId == EditContactFragment.ARG_VALUE_NO_CONTACT_ID) {
            // Insert new Contact
            // TODO maybe validate by making sure there's a name or picture
            int id = getNextContactId();
            contact = new Contact(id, name, imageAbsolutePath, true);
            mRepository.insert(contact);
        } else {
            // Update existing Contact
            // get Contact's isEnabled value
            Contact oldContact = mRepository.getContactSync(contactId);
            boolean isEnabled = oldContact.getIsEnabled();
            // update contact
            contact = new Contact(contactId, name, imageAbsolutePath, isEnabled);
            mRepository.update(contact);
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
