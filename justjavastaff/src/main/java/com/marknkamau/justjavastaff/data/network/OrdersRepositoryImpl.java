package com.marknkamau.justjavastaff.data.network;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.marknkamau.justjavastaff.models.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public class OrdersRepositoryImpl implements OrdersRepository {
    private FirebaseFirestore firestore;

    public OrdersRepositoryImpl() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        firestore.setFirestoreSettings(settings);
    }

    @Override
    public void getOrders(final OrdersListener listener) {
        ListenerRegistration registration = firestore.collection("orders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot querySnapshot, FirebaseFirestoreException e) {
                        if (e != null) {
                            Timber.e(e);
                            listener.onError(e.getMessage());
                        } else {
                            List<Order> orders = new ArrayList<>();
                            List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();

                            for (DocumentSnapshot document : documentSnapshots) {
                                Map<String, Object> data = document.getData();
                                Order order = new Order(
                                        (String) data.get("orderId"),
                                        (String) data.get("customerName"),
                                        (String) data.get("customerPhone"),
                                        (String) data.get("deliveryAddress"),
                                        (String) data.get("additionalComments"),
                                        (String) data.get("status"),
                                        (Date) data.get("timestamp"),
                                        (int) (long) data.get("totalPrice"),
                                        (int) (long) data.get("itemsCount")
                                );

                                orders.add(order);
                            }

                            listener.onSuccess(orders);
                        }
                    }
                });
    }

}
