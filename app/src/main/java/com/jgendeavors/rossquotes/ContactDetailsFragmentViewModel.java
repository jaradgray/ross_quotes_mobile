package com.jgendeavors.rossquotes;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ContactDetailsFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private MessageRepository mRepository;    private LiveData<List<Message>> mMessagesForContact;

    // Constructor
    public ContactDetailsFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MessageRepository(application);
    }

    // API methods
    public LiveData<List<Message>> getMessagesForContact(int contactId) {
        return mRepository.getMessagesForContact(contactId);
    }


}
