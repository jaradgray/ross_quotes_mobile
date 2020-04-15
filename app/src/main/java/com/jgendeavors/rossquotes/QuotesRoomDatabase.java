package com.jgendeavors.rossquotes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
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

                    // absolute path to resources folder. Add res id to the path to get path to the resource
                    // Used to set bundled Contacts' profile photos
                    String resPath = "android.resource://com.jgendeavors.rossquotes/";

                    // Create Bob Ross as a Contact and add him to the db
                    final int CONTACT_ID_BOB_ROSS = 1;
                    Contact bob = new Contact(CONTACT_ID_BOB_ROSS, "Bob Ross", resPath + R.drawable.profile_ross, true);
                    contactDao.insert(bob);

                    // Messages
                    MessageDao messageDao = INSTANCE.messageDao();

                    // Bob Ross Messages
                    // TODO add the real ones
                    List<Message> messages = new ArrayList<>();
                    messages.add(new Message(CONTACT_ID_BOB_ROSS, "Happy little wolves!", false));
                    messages.add(new Message(CONTACT_ID_BOB_ROSS, "Lovely day we're having!", false));
                    messages.add(new Message(CONTACT_ID_BOB_ROSS, "Top o' the mornin' to ya!", false));

                    // insert Messages into db
                    messageDao.insert(messages);

                    // TODO remember to initialize db here in onCreate as per the latest db version for fresh installs.
                    //  Then add Migrations from previous versions to the latest version, for current users
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


    /** Example Migration for adding a new Contact and Messages */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // add Rick Steves to the contact_table
            database.execSQL("INSERT INTO contact_table (id, name, image_absolute_path, is_enabled) "
                    + " VALUES (2, 'Rick Steves', 'android.resource://com.jgendeavors.rossquotes/drawable/profile_steves', 1)");
            // add Rick Steves' quotes to the message_table
            database.execSQL("INSERT INTO message_table (contact_id, text, is_recently_used) "
                    + " VALUES "
                    + " (2, 'Welcome!', 0), "
                    + " (2, 'Keep on traveling!', 0), "
                    + " (2, 'Dublin', 0), "
                    + " (2, 'Ciao!', 0)");
        }
    };

    /** Example Migration for adding Messages for a Contact */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // add quotes to the message_table
            database.execSQL("INSERT INTO message_table (contact_id, text, is_recently_used) "
                    + " VALUES "
                    + " (1, 'Hi, this is Bob!', 0), "
                    + " (1, 'You can do anything.', 0), "
                    + " (1, 'Here in your world', 0), "
                    + " (1, 'Happy painting.', 0)");
        }
    };
}
