package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * The MessageDao class handles database operations for Message entities.
 */
@Dao
public interface MessageDao {
    @Insert
    void insert(List<Message> messages);

    @Insert
    long insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT * FROM message_table WHERE contact_id = :contactId")
    LiveData<List<Message>> getMessagesForContact(int contactId);

    @Query("SELECT * FROM message_table WHERE id = :id")
    Message getMessage(int id);

    @Query("SELECT * FROM message_table")
    List<Message> getAllSync();
}
