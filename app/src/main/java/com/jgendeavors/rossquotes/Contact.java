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


    // Constructor

    public Contact(String name, String imageAbsolutePath) {
        mName = name;
        mImageAbsolutePath = imageAbsolutePath;
    }


    // Setters
    public void setId(int id) { mId = id; }


    // Getters
    public int getId() { return mId; }
    public String getName() { return mName; }
    public String getImageAbsolutePath() { return mImageAbsolutePath; }
}
