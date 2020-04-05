package com.jgendeavors.rossquotes;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

/**
 * The Repository class is in the Android recommended architecture.
 * It provides a single point of access to potentially more than one data source.
 * However, in this app we have only one data source (the Room database)
 *
 * The Repository provides our ViewModel with a clean API for accessing our app's data
 */
public class ContactRepository {
    private static final String TAG = "ContactRepository";


    // Instance variables
    private ContactDao mContactDao;
    private LiveData<List<Contact>> mAlphabetizedContacts;


    // Constructor
    public ContactRepository(Context context) {
        QuotesRoomDatabase database = QuotesRoomDatabase.getInstance(context);
        mContactDao = database.contactDao();
        mAlphabetizedContacts = mContactDao.getAlphabetizedContacts(); // Room automatically executes database operations that return LiveData on a background thread
    }


    // API methods

    public void insert(Contact contact) { new InsertContactAsyncTask(mContactDao).execute(contact); }

    public void update(Contact contact) { new UpdateContactAsyncTask(mContactDao).execute(contact); }

    public void delete(int contactId) { new DeleteContactAsyncTask(mContactDao).execute(contactId); }

    public LiveData<Contact> getContact(int id) { return mContactDao.getContact(id); }

    public LiveData<List<Contact>> getAlphabetizedContacts() { return mAlphabetizedContacts; }

    /**
     * Returns the Contact with the given id from the database synchronously.
     * Simplified and based on MessageRepository.getAllSync()
     *
     * @param id
     * @return
     */
    public Contact getContactSync(final int id) {
        // Create ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Create the task the ExecutorService will execute
        Callable<Contact> callableTask = new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return mContactDao.getContactSync(id);
            }
        };

        // Return the result of the task when it completes
        Contact result = null;
        try {
            result = executorService.submit(callableTask).get();
        } catch (Exception e) {
            Log.e(TAG, "ContactRepository.getContactSync(): " + e);
        }
        return result;
    }


    // AsyncTasks for performing database operations on a background thread
    // Note: they are static so they don't have a reference to the Repository itself, which could create memory leaks

    /** Wraps ContactDao.insert(Contact) */
    private static class InsertContactAsyncTask extends AsyncTask<Contact, Void, Long> {
        // Instance variables
        private ContactDao contactDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public InsertContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        // Overridden methods
        @Override
        protected Long doInBackground(Contact... contacts) {
            return contactDao.insert(contacts[0]);
        }
    }

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

    /** Wraps ContactDao.delete(int) */
    private static class DeleteContactAsyncTask extends AsyncTask<Integer, Void, Void> {
        // Instance variables
        private ContactDao contactDao; // since this AsyncTask is static, it doesn't have access to the repository's DAO

        // Constructor
        public DeleteContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        // Overridden methods
        @Override
        protected Void doInBackground(Integer... integers) {
            contactDao.delete(integers[0]);
            return null;
        }
    }
}
