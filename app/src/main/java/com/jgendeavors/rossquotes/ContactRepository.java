package com.jgendeavors.rossquotes;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * The Repository class is in the Android recommended architecture.
 * It provides a single point of access to potentially more than one data source.
 * However, in this app we have only one data source (the Room database)
 *
 * The Repository provides our ViewModel with a clean API for accessing our app's data
 */
public class ContactRepository {
    // Instance variables
    private ContactDao mContactDao;
    private LiveData<List<Contact>> mAlphabetizedContacts;


    // Constructor
    public ContactRepository(Application application) {
        QuotesRoomDatabase database = QuotesRoomDatabase.getInstance(application);
        mContactDao = database.contactDao();
        mAlphabetizedContacts = mContactDao.getAlphabetizedContacts(); // Room automatically executes database operations that return LiveData on a background thread
    }


    // API methods

    public void update(Contact contact) { new UpdateContactAsyncTask(mContactDao).execute(contact); }

    public LiveData<Contact> getContact(int id) { return mContactDao.getContact(id); }

    public LiveData<List<Contact>> getAlphabetizedContacts() { return mAlphabetizedContacts; }


    // AsyncTasks for performing database operations on a background thread
    // Note: they are static so they don't have a reference to the Repository itself, which could create memory leaks

    /** Wraps ContactDao.update(Contact) */
    private static class UpdateContactAsyncTask extends AsyncTask<Contact, Void, Void> {
        // Instance variables
        private ContactDao contactDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public UpdateContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        // Overridden methods
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.update(contacts[0]);
            return null;
        }
    }
}
