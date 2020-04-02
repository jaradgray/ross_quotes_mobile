package com.jgendeavors.rossquotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class EditMessageFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private MessageRepository mRepository;


    // Constructor
    public EditMessageFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MessageRepository(application);
    }


    // API methods
    public Message getMessage(int messageId) { return mRepository.getMessage(messageId); }
}
