package com.jgendeavors.rossquotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class EditMessageFragmentViewModel extends AndroidViewModel {
    // Instance variables
    private MessageRepository mRepository;
    private MutableLiveData<Message> mMessage;


    // Constructor
    public EditMessageFragmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MessageRepository(application);
        mMessage = new MutableLiveData<>();
    }


    // API methods
    public void setMessageById(int messageId) { mMessage.postValue(mRepository.getMessage(messageId)); }
    public LiveData<Message> getMessage() { return mMessage; }
}
