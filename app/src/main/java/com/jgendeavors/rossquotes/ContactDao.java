package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * The DAO (Data Access Object) provides abstracted access to the Room database
 * We define methods that correspond to database operations, and Room implements the methods
 * under the hood.
 *
 * Rule of thumb: create one DAO per Room entity
 *
 * The ContactDao class handles database operations for Contact entities.
 */
@Dao
public interface ContactDao {
    @Insert
    void insert(Contact contact);

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAlphabetizedContacts();
}
