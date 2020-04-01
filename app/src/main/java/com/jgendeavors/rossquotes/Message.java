package com.jgendeavors.rossquotes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The Message class represents a message that a contact can send.
 */
@Entity(tableName = "message_table")
public class Message {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "contact_id")
    private int mContactId;

    @ColumnInfo(name = "text")
    private String mText;


    // Constructor
    public Message(int contactId, String text) {
        mContactId = contactId;
        mText = text;
    }


    // Setters
    public void setId(int id) { mId = id; }


    // Getters
    public int getId() { return mId; }
    public int getContactId() { return mContactId; }
    public String getText() { return mText; }
}
