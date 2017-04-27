package com.marknkamau.justjava.utils;

import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * This class ensures that an instance to the database is not created multiple times.
 * It also prevents '.setPersistenceEnabled(true)' from crashing due to an IllegalStateException
 */

public class FirebaseDBUtil {
    private static FirebaseDatabase mDatabase;

    //TODO remove
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    static {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
    }

    public static void getUserDefaults(final UserDetailsListener listener){
        final DatabaseReference database = FirebaseDBUtil.getDatabase().getReference().child("users/" + FirebaseAuthUtils.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.dataReceived(dataSnapshot.child("name").getValue().toString(),
                        dataSnapshot.child("phone").getValue().toString(),
                        dataSnapshot.child("defaultAddress").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.actionFailed(databaseError.getMessage());
            }
        });
    }

    public interface UserDetailsListener {
        void dataReceived(String name, String phone, String deliveryAddress);
        void actionFailed(String response);
    }
}
