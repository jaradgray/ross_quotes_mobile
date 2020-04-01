package com.jgendeavors.rossquotes;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * The Repository class is in the Android recommended architecture.
 * It provides a single point of access to potentially more than one data source.
 * However, in this app we have only one data source (the Room database)
 *
 * The Repository provides our ViewModel with a clean API for accessing our app's data
 */
public class MessageRepository {
    // Instance variables
    private MessageDao mMessageDao;


    // Constructor
    public MessageRepository(Application application) {
        QuotesRoomDatabase database = QuotesRoomDatabase.getInstance(application);
        mMessageDao = database.messageDao();
    }


    // API methods

    public LiveData<List<Message>> getMessagesForContact(int contactId) {
        return mMessageDao.getMessagesForContact(contactId); // Room automatically executes database operations that return LiveData on a background thread
    }
}
