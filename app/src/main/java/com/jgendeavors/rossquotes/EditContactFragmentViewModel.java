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
}
