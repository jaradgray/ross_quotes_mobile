package com.jgendeavors.rossquotes;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ReceivedMessagesFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private ReceivedMessageRepository mRepository;
    private LiveData<List<ReceivedMessage>> mAllReceivedMessages;


    // Constructor
    public ReceivedMessagesFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ReceivedMessageRepository(application);
        mAllReceivedMessages = mRepository.getAllReceivedMessages();
    }


    // API methods

    public LiveData<List<ReceivedMessage>> getAllReceivedMessages() { return mAllReceivedMessages; }
    public void deleteAllReceivedMessages() { mRepository.deleteAll(); }
}
