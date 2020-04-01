package com.jgendeavors.rossquotes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The ReceivedMessage class represents a message the user receives via notification.
 */
@Entity(tableName = "received_message_table")
public class ReceivedMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "message_id")
    private int mMessageId;

    @ColumnInfo(name = "contact_id")
    private int mContactId;

    @ColumnInfo(name = "timestamp")
    private long mTimestamp;


    // Constructor
    public ReceivedMessage(int messageId, int contactId, long timestamp) {
        mMessageId = messageId;
        mContactId = contactId;
        mTimestamp = timestamp;
    }


    // Setters
    public void setId(int id) { mId = id; }


    // Getters
    public int getId() { return mId; }
    public int getMessageId() { return mMessageId; }
    public int getContactId() { return mContactId; }
    public long getTimestamp() { return mTimestamp; }
}
