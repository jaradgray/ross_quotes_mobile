package com.jgendeavors.rossquotes;

import androidx.room.Dao;
import androidx.room.Insert;

/**
 * The ContactDao class handles database operations for Contact entities.
 */
@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    // TODO add database queries here
}
