package com.jgendeavors.rossquotes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * The RoomDatabase for our app.
 * Based on the Android "Room With A View" tutorial: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#6
 *
 * Singleton pattern
 */
@Database(entities = {Contact.class, Message.class, ReceivedMessage.class}, version = 1, exportSchema = false)
public abstract class QuotesRoomDatabase extends RoomDatabase {
    private static final String TAG = "QuotesRoomDatabase";

    public static final String DB_NAME = "quotes_room_database";

    // Room implements these methods that return Dao objects under the hood
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();
    public abstract ReceivedMessageDao receivedMessageDao();

    private static volatile QuotesRoomDatabase INSTANCE;
    public static final int NUM_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    public static QuotesRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuotesRoomDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, QuotesRoomDatabase.class, DB_NAME)
                        .addCallback(sRoomDatabaseCallback)
                        .build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.d(TAG, "RoomDatabase.Callback onCreate: started.");
            super.onCreate(db);

            // Populate database in the background
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    // Contacts
                    ContactDao contactDao = INSTANCE.contactDao();

                    contactDao.insert(new Contact("Bob Ross"));
                    contactDao.insert(new Contact("Rick Steves"));

                    // Messages
                    MessageDao messageDao = INSTANCE.messageDao();

                    // TODO add the real Messages
                    List<Message> messages = new ArrayList<>();

                    messages.add(new Message("Hello there!"));
                    messages.add(new Message("Lovely day we're having!"));
                    messages.add(new Message("Top o' the mornin' to ya!"));
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            Log.d(TAG, "RoomDatabase.Callback onOpen: started.");
            super.onOpen(db);
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            Log.d(TAG, "RoomDatabase.Callback onDestructiveMigration: started.");
            super.onDestructiveMigration(db);
        }
    };
}
