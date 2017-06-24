package com.marknkamau.justjavastaff.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public interface BaseDBListener {
    void eventListenerObtained(DatabaseReference databaseReference, ValueEventListener eventListener);
}
