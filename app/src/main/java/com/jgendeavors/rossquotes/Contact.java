package com.jgendeavors.rossquotes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The Contact class represents a contact in our app.
 */
@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "image_absolute_path")
    private String mImageAbsolutePath;

    @ColumnInfo(name = "is_enabled")
    private boolean mIsEnabled;


    // Constructor

    public Contact(String name, String imageAbsolutePath, boolean isEnabled) {
        mName = name;
        mImageAbsolutePath = imageAbsolutePath;
        mIsEnabled = isEnabled;
    }


    // Setters
    public void setId(int id) { mId = id; }
    public void setIsEnabled(boolean value) { mIsEnabled = value; }


    // Getters
    public int getId() { return mId; }
    public String getName() { return mName; }
    public String getImageAbsolutePath() { return mImageAbsolutePath; }
    public boolean getIsEnabled() { return mIsEnabled; }
}
