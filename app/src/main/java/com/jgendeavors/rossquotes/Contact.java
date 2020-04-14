package com.jgendeavors.rossquotes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The Contact class represents a contact in our app.
 */
@Entity(tableName = "contact_table")
public class Contact {
    // Constants
    @Ignore
    public static final int FIRST_FREE_CONTACT_ID = 100; // ids <100 are reserved for factory-installed Contacts


    // Instance variables

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "image_absolute_path")
    private String mImageAbsolutePath;

    @ColumnInfo(name = "is_enabled")
    private boolean mIsEnabled;


    // Constructor

    public Contact(int id, String name, String imageAbsolutePath, boolean isEnabled) {
        mId = id;
        mName = name;
        mImageAbsolutePath = imageAbsolutePath;
        mIsEnabled = isEnabled;
    }


    // Setters
    public void setIsEnabled(boolean value) { mIsEnabled = value; }


    // Getters
    public int getId() { return mId; }
    public String getName() { return mName; }
    public String getImageAbsolutePath() { return mImageAbsolutePath; }
    public boolean getIsEnabled() { return mIsEnabled; }
}
