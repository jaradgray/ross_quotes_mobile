package com.jgendeavors.rossquotes;

import android.app.Application;

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
    public void insertOrUpdateContact(int contactId, String name) {
        Contact contact;
        if (contactId == EditContactFragment.ARG_VALUE_NO_CONTACT_ID) {
            // Insert new Contact
            // TODO maybe validate by making sure there's a name or picture
            contact = new Contact(name, null /* TODO implement*/);
            mRepository.insert(contact);
        } else {
            // Update existing Contact
            contact = new Contact(name, null /* TODO implement */);
            contact.setId(contactId);
            mRepository.update(contact);
        }
    }
}
