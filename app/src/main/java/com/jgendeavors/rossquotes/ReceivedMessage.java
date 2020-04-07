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

    @ColumnInfo(name = "contact_name")
    private String mContactName;

    @ColumnInfo(name = "contact_image_absolute_path")
    private String mContactImageAbsolutePath;

    @ColumnInfo(name = "message_text")
    private String mMessageText;

    @ColumnInfo(name = "timestamp")
    private long mTimestamp;


    // Constructor
    public ReceivedMessage(String contactName, String contactImageAbsolutePath, String messageText, long timestamp) {
        mContactName = contactName;
        mContactImageAbsolutePath = contactImageAbsolutePath;
        mMessageText = messageText;
        mTimestamp = timestamp;
    }


    // Setters
    public void setId(int id) { mId = id; }


    // Getters
    public int getId() { return mId; }
    public String getContactName() { return mContactName; }
    public String getContactImageAbsolutePath() { return mContactImageAbsolutePath; }
    public String getMessageText() { return mMessageText; }
    public long getTimestamp() { return mTimestamp; }
}
