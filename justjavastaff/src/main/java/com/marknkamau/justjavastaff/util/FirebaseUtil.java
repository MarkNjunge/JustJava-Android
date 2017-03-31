package com.marknkamau.justjavastaff.util;

import com.google.firebase.database.FirebaseDatabase;

/**
 * This class ensures that an instance to the database is not created multiple times.
 * It also prevents '.setPersistenceEnabled(true)' from crashing due to an IllegalStateException
 */

public class FirebaseUtil {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
