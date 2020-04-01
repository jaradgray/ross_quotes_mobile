package com.jgendeavors.rossquotes;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * The ViewModel class is responsible for storing and processing data that affects the UI, and
 * for communicating user interaction to the model (Repository)
 *
 * Data stored in the ViewModel survives configuration changes, unlike data stored in an Activity or Fragment
 *
 * Remember, ViewModel outlives Activities and Fragments, so don't store references to Activity Contexts
 * or to Views that reference the Activity Context in the ViewModel. We need to pass an Application
 * (Context subclass) to our Repository, so we extend AndroidViewModel whose constructor provides one.
 */
public class ContactsFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private ContactRepository mRepository;
    private LiveData<List<Contact>> mContacts;


    // Constructor
    public ContactsFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ContactRepository(application);
        mContacts = mRepository.getAlphabetizedContacts();
    }


    // Getter
    public LiveData<List<Contact>> getContacts() { return mContacts; }


    // TODO add data manipulation methods
    // Data manipulation methods
    // Our Fragment only has access to the ViewModel and not the Repository, so we create
    // wrapper methods for the Repository API
}
