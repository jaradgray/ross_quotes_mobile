package com.jgendeavors.rossquotes;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

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
    private static final String TAG = "MessageRepository";


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

    /**
     * Given a Message's id, returns the corresponding Message from the database.
     *
     * @param messageId
     * @return
     */
    public Message getMessage(int messageId) {
        Message result = null;
        try {
            result = new GetMessageAsyncTask(mMessageDao).execute(messageId).get();
        } catch (Exception e) {
            Log.e(TAG, "MessageRepository.getMessage(): " + e);
        }
        return result;
    }

    public void update(Message message) {
        new UpdateMessageAsyncTask(mMessageDao).execute(message);
    }

    public void delete(Message message) {
        new DeleteMessageAsyncTask(mMessageDao).execute(message);
    }


    // AsyncTasks for performing database operations on a background thread
    // Note: they are static so they don't have a reference to the Repository itself, which could create memory leaks

    /**
     * Wraps MessageDao.getMessage(int)
     */
    private static class GetMessageAsyncTask extends AsyncTask<Integer, Void, Message> {
        // Instance variables
        private MessageDao messageDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public GetMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        // Overridden methods
        @Override
        protected Message doInBackground(Integer... integers) {
            return messageDao.getMessage(integers[0]);
        }
    }

    /**
     * Wraps MessageDao.update(Message)
     */
    private static class UpdateMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        // Instance variables
        private MessageDao messageDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public UpdateMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        // Overridden methods
        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.update(messages[0]);
            return null;
        }
    }

    /**
     * Wraps messageDao.delete(Message)
     */
    private static class DeleteMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        // Instance variables
        private MessageDao messageDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public DeleteMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        // Overridden methods
        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.delete(messages[0]);
            return null;
        }
    }
}
