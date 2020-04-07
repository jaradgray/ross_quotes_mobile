package com.jgendeavors.rossquotes;

import android.content.Context;

import java.util.List;

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

    public LiveData<List<ReceivedMessage>> getAllReceivedMessages() { return mAllReceivedMessages; }
}
