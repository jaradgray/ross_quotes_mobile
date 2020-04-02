package com.jgendeavors.rossquotes;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ContactDetailsFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private MessageRepository mMessageRepository;
    private ContactRepository mContactRepository;

    // Constructor
    public ContactDetailsFragmentViewModel(@NonNull Application application) {
        super(application);

        mMessageRepository = new MessageRepository(application);
        mContactRepository = new ContactRepository(application);
    }

    // API methods
    public LiveData<Contact> getContact(int contactId) {
        return mContactRepository.getContact(contactId);
    }

    public LiveData<List<Message>> getMessagesForContact(int contactId) {
        return mMessageRepository.getMessagesForContact(contactId);
    }

    /** Deletes the Contact with the given id from the database, via the Repository */
    public void deleteContact(int contactId) {
        mContactRepository.delete(contactId);
    }


}
