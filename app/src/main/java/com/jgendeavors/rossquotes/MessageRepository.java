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

    /**
     * Returns a List of all Messages in the database.
     * Database operations must execute on a background thread, but this method blocks until the operation completes.
     *
     * @return
     */
    public List<Message> getAllSync() {
        // Decided to use ExecutorService instead of AsyncTask since AsyncTask is stupid and deprecated.
        //  based on this example: https://howtodoinjava.com/java/multi-threading/executor-service-example/

        // TODO there's probably some way to make this simpler...

        // Get an ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Submit a value-returning task for execution, store the pending result of the task in a Future object
        Future<List<Message>> future = executorService.submit(new Callable<List<Message>>() {
            @Override
            public List<Message> call() throws Exception {
                return mMessageDao.getAllSync();
            }
        });

        // Return the result of the submitted task
        List<Message> result = null;
        try {
            result = future.get(); // Future.get() waits for the operation to complete, similar to AsyncTask.execute().get()
        } catch (Exception e) {
            Log.e(TAG, "MessageRepository.getAllSync(): " + e);
        }
        return result;
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
