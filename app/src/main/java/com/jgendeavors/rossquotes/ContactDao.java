package com.jgendeavors.rossquotes;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    List<Long> insert(List<Contact> contacts);

    @Insert
    long insert(Contact contact);

    @Update
    void update(Contact contact);

    @Query("DELETE FROM contact_table WHERE id = :contactId")
    void delete(int contactId);

    @Query("SELECT * FROM contact_table WHERE id = :id")
    LiveData<Contact> getContact(int id);

    @Query("SELECT * FROM contact_table ORDER BY name ASC")
    LiveData<List<Contact>> getAlphabetizedContacts();
}
