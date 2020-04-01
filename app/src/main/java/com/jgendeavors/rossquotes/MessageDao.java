package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * The MessageDao class handles database operations for Message entities.
 */
@Dao
public interface MessageDao {
    @Insert
    void insert(List<Message> messages);

    @Query("SELECT * FROM message_table WHERE contact_id = :contactId")
    LiveData<List<Message>> getMessagesForContact(int contactId);
}
