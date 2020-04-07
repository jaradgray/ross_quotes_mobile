package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

/**
 * The ReceivedMessageDao class handles database operations for ReceivedMessage entities.
 */
@Dao
public interface ReceivedMessageDao {
    @Query("SELECT * FROM received_message_table ORDER BY timestamp DESC")
    LiveData<List<ReceivedMessage>> getAllByTimestampDesc();
}
