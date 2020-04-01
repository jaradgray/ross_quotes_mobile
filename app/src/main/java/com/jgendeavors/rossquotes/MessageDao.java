package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;

/**
 * The MessageDao class handles database operations for Message entities.
 */
@Dao
public interface MessageDao {
    @Insert
    void insert(List<Message> messages);

    // TODO add database queries here
}
