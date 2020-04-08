package com.jgendeavors.rossquotes;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class ReceivedMessageRepository {
    // Instance variables
    private ReceivedMessageDao mReceivedMessageDao;
    private LiveData<List<ReceivedMessage>> mAllReceivedMessages;


    // Constructor
    public ReceivedMessageRepository(Context context) {
        QuotesRoomDatabase database = QuotesRoomDatabase.getInstance(context);
        mReceivedMessageDao = database.receivedMessageDao();
        mAllReceivedMessages = mReceivedMessageDao.getAllByTimestampDesc();
    }


    // API methods

    /**
     * Wraps ReceivedMessageDao.insert(ReceivedMessage)
     * (Database operations that don't return LiveData objects must be explicitly executed on a background thread)
     *
     * @param receivedMessage
     */
    public void insert(final ReceivedMessage receivedMessage) {
        // Get an ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute (the task won't return a result, so we can use Runnable)
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mReceivedMessageDao.insert(receivedMessage);
            }
        };

        // Execute the task at some time in the future, via ExecutorService
        executorService.execute(task);
    }

    public LiveData<List<ReceivedMessage>> getAllReceivedMessages() { return mAllReceivedMessages; }

    /** Wraps ReceivedMessageDao.deleteAll() */
    public void deleteAll() {
        // Get an ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute (the task won't return a result, so we can use Runnable)
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mReceivedMessageDao.deleteAll();
            }
        };

        // Execute the task at some time in the future, via ExecutorService
        executorService.execute(task);
    }
}
