package com.marknkamau.justjavastaff.util;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marknkamau.justjavastaff.models.CustomerOrder;

import java.util.ArrayList;
import java.util.List;

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

    static {
        getDatabase();
    }

    public static void getAllOrders(final DBAllOrdersListener listener) {
        DatabaseReference databaseReference = mDatabase.getReference().child("allOrders").orderByKey().getRef();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CustomerOrder> customerOrders = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("MNK", dataSnapshot.toString());
                    customerOrders.add(new CustomerOrder(
                            (String) snapshot.child("orderID").getValue()
                            , String.valueOf(snapshot.child("customerName").getValue())
                            , String.valueOf(snapshot.child("customerPhone").getValue())
                            , String.valueOf(snapshot.child("deliveryAddress").getValue())
                            , String.valueOf(snapshot.child("additionalComments").getValue())
                            , String.valueOf(snapshot.child("currentStatus").getValue())
                            , String.valueOf(snapshot.child("timestamp").getValue())
                            , String.valueOf(snapshot.child("totalPrice").getValue())
                            , String.valueOf(snapshot.child("itemsCount").getValue())));

                }
                listener.ordersObtained(customerOrders);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.errorOccurred(databaseError.getMessage());
            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        listener.eventListenerObtained(databaseReference, valueEventListener);
    }

    public interface DBAllOrdersListener extends BaseDBListener {
        void ordersObtained(List<CustomerOrder> orders);

        void errorOccurred(String response);
    }
}
