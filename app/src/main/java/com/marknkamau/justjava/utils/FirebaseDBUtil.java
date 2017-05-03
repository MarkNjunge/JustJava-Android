package com.marknkamau.justjava.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.marknkamau.justjava.models.CartItem;
import com.marknkamau.justjava.models.PreviousOrder;
import com.marknkamau.justjava.models.UserDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class ensures that an instance to the database is not created multiple times.
 * It also prevents '.setPersistenceEnabled(true)' from crashing due to an IllegalStateException
 */

public class FirebaseDBUtil {
    public static final String CUSTOMER_NAME = "customerName";
    public static final String CUSTOMER_PHONE = "customerPhone";
    public static final String ITEMS_COUNT = "itemsCount";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String COMMENTS = "additionalComments";
    public static final String ADDRESS = "deliveryAddress";
    private static FirebaseDatabase database;

    private static List<PreviousOrder> previousOrders;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    static {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
    }

    public static void getUserDefaults(final UserDetailsListener listener) {
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

    public static void placeNewOrder(final Map<String, Object> orderDetails, List<CartItem> cartItems, final PlaceOrderListener listener) {
        DatabaseReference orderRef = database.getReference().child("allOrders").push();
        final String key = orderRef.getKey();

        orderRef.child("orderID").setValue(key);
        orderRef.child("customerName").setValue(orderDetails.get(CUSTOMER_NAME));
        orderRef.child("customerPhone").setValue(orderDetails.get(CUSTOMER_PHONE));
        orderRef.child("itemsCount").setValue(orderDetails.get(ITEMS_COUNT));
        orderRef.child("totalPrice").setValue(orderDetails.get(TOTAL_PRICE));
        orderRef.child("orderStatus").setValue("Pending");
        orderRef.child("deliveryAddress").setValue(orderDetails.get(ADDRESS));
        orderRef.child("additionalComments").setValue(orderDetails.get(COMMENTS));
        orderRef.child("deviceToken").setValue(FirebaseInstanceId.getInstance().getToken());
        orderRef.child("timestamp").setValue(ServerValue.TIMESTAMP);

        FirebaseUser currentUser = FirebaseAuthUtils.getCurrentUser();
        if (currentUser != null) {
            orderRef.child("user").setValue(currentUser.getUid());
            DatabaseReference userOrdersRef = database.getReference().child("userOrders/" + currentUser.getUid());
            userOrdersRef.push().setValue(key);
        } else {
            orderRef.child("user").setValue("null");
        }

        DatabaseReference orderItemsRef = database.getReference().child("orderItems").child(key);
        orderItemsRef.setValue(cartItems).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.orderSuccessful();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.orderFailed(e.getMessage());
            }
        });
    }

    public static void getPreviousOrders(final PreviousOrdersListener listener) {
        previousOrders = new ArrayList<>();
        database.getReference().child("userOrders/" + FirebaseAuthUtils.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            listener.noValuesPresent();
                        } else {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Query orderItem = database.getReference().child("allOrders/" + snapshot.getValue());
                                orderItem.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        try {
                                            previousOrders.add(new PreviousOrder(
                                                    snapshot.child("deliveryAddress").getValue().toString()
                                                    , snapshot.child("timestamp").getValue().toString()
                                                    , snapshot.child("totalPrice").getValue().toString()
                                                    , snapshot.child("orderStatus").getValue().toString())
                                            );
                                        } catch (Exception e) {
                                            listener.actionFailed(e.getMessage());
                                        }
                                        listener.valuesRetrieved(previousOrders);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        listener.actionFailed(databaseError.getMessage());
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.actionFailed(databaseError.getMessage());
                    }
                });
    }

    public static void setUserDefaults(UserDefaults userDefaults, final SetUserDefaultsListener listener) {
        DatabaseReference databaseReference = FirebaseDBUtil.getDatabase().getReference().child("users/" + FirebaseAuthUtils.getCurrentUser().getUid());
        databaseReference.setValue(userDefaults).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.taskSuccessful();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.taskFailed(e.getMessage());
            }
        });

    }

    public interface UserDetailsListener {
        void dataReceived(String name, String phone, String deliveryAddress);

        void actionFailed(String response);
    }

    public interface PlaceOrderListener {
        void orderSuccessful();

        void orderFailed(String response);
    }

    public interface SetUserDefaultsListener {
        void taskSuccessful();

        void taskFailed(String response);
    }

    public interface PreviousOrdersListener {
        void valuesRetrieved(List<PreviousOrder> previousOrders);

        void noValuesPresent();

        void actionFailed(String response);
    }

}
