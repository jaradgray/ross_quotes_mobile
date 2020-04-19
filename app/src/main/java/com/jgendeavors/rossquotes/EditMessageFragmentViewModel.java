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

    /**
     * Inserts or updates a Message
     * in the database, via the Repository.
     * @param text
     */
    public void saveMessage(int contactId, String text) {
        Message message;
        if (mMessage.getValue() == null) {
            // Save new Message
            message = new Message(contactId, text, false);
            int id = (int)mRepository.insert(message);
            message.setId(id);
        } else {
            // Update existing Message
            Message oldMessage = mMessage.getValue();
            message = new Message(oldMessage.getContactId(), text, oldMessage.getIsRecentlyUsed());
            message.setId(oldMessage.getId());
            mRepository.update(message);
        }
        // update this ViewModel's mMessage
        mMessage.postValue(message);
    }

    /**
     * Deletes the Message we're dealing with from the database, via the Repository
     */
    public void deleteMessage() {
        if (mMessage.getValue() == null) return;
        mRepository.delete(mMessage.getValue());
    }
}
