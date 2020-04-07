package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * The ReceivedMessageDao class handles database operations for ReceivedMessage entities.
 */
@Dao
public interface ReceivedMessageDao {
    @Insert
    void insert(ReceivedMessage receivedMessage);

    @Query("SELECT * FROM received_message_table ORDER BY timestamp DESC")
    LiveData<List<ReceivedMessage>> getAllByTimestampDesc();
}
