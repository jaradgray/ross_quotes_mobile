package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public MessageRepository(Context context) {
        QuotesRoomDatabase database = QuotesRoomDatabase.getInstance(context);
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

    public long insert(Message message) {
        long result = -1;
        try {
            result = new InsertMessageAsyncTask(mMessageDao).execute(message).get();
        } catch (Exception e) {
            Log.e(TAG, "MessageRepository.insert(): " + e);
        }
        return result;
    }

    public void update(Message message) {
        new UpdateMessageAsyncTask(mMessageDao).execute(message);
    }

    public void delete(Message message) {
        new DeleteMessageAsyncTask(mMessageDao).execute(message);
    }

    /** Wraps MessageDao.deleteByContactId(int) */
    public void deleteByContactId(final int contactId) {
        // Get an ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute (the task won't return a result, so we can use Runnable)
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mMessageDao.deleteByContactId(contactId);
            }
        };

        // Execute the task at some time in the future, via ExecutorService
        executorService.execute(task);
    }

    /**
     * Returns all Messages in the database with the given @contactId synchronously.
     * (Database operations must execute on a background thread, but this method blocks until the operation completes.)
     *
     * @param contactId
     * @return
     */
    public List<Message> getAllMessagesForContactSync(final int contactId) {
        // Decided to use ExecutorService instead of AsyncTask since AsyncTask is stupid and deprecated.
        //  based on this example: https://howtodoinjava.com/java/multi-threading/executor-service-example/

        // Create ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute
        Callable<List<Message>> callableTask = new Callable<List<Message>>() {
            @Override
            public List<Message> call() throws Exception {
                return mMessageDao.getMessagesForContactSync(contactId);
            }
        };

        // Return the result of the task when it completes
        List<Message> result = null;
        try {
            result = executorService.submit(callableTask).get();
        } catch (Exception e) {
            Log.e(TAG, "MessageRepository.getMessagesForContactSync(): " + e);
        }
        return result;
    }

    /**
     * Wraps MessageDao.getMessagesForContactSync(int, boolean)
     * Returns all Messages in the database with the given @contactId and whose isRecentlyUsed member is false, synchronously.
     */
    public List<Message> getUnusedMessagesForContactSync(final int contactId) {
        // Create ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute
        Callable<List<Message>> callableTask = new Callable<List<Message>>() {
            @Override
            public List<Message> call() throws Exception {
                return mMessageDao.getMessagesForContactSync(contactId, false);
            }
        };

        // Return the result of the task when it completes
        List<Message> result = null;
        try {
            result = executorService.submit(callableTask).get();
        } catch (Exception e) {
            Log.e(TAG, "MessageRepository.getUnusedMessagesForContactSync(): " + e);
        }
        return result;
    }


    // AsyncTasks for performing database operations on a background thread
    // Note: they are static so they don't have a reference to the Repository itself, which could create memory leaks

    /** Wraps MessageDao.getMessage(int) */
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

    /** Wraps MessageDao.insert(Message) */
    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Long> {
        // Instance variables
        private MessageDao messageDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public InsertMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        // Overridden methods
        @Override
        protected Long doInBackground(Message... messages) {
            return messageDao.insert(messages[0]);
        }
    }

    /** Wraps MessageDao.update(Message) */
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

    /** Wraps messageDao.delete(Message) */
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
