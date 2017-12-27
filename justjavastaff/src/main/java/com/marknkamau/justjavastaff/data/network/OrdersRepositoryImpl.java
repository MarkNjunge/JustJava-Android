package com.marknkamau.justjavastaff.data.network;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.marknkamau.justjavastaff.models.Order;
import com.marknkamau.justjavastaff.models.OrderItem;
import com.marknkamau.justjavastaff.models.OrderStatus;

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

    @Override
    public void getOrderItems(String orderId, final OrderItemsListener listener) {
        firestore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<OrderItem> items = new ArrayList<>();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();

                        for (DocumentSnapshot document : documents) {
                            Map<String, Object> data = document.getData();
                            OrderItem orderItem = new OrderItem(
                                    data.get("itemName").toString(),
                                    (int) (long) data.get("itemQty"),
                                    (Boolean) data.get("itemCinnamon"),
                                    (Boolean) data.get("itemChoc"),
                                    (Boolean) data.get("itemMarshmallow"),
                                    (int) (long) data.get("itemPrice")
                            );
                            items.add(orderItem);
                        }

                        listener.onSuccess(items);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e);
                        listener.onError(e.getMessage());
                    }
                });
    }

    @Override
    public void updateOderStatus(String orderId, OrderStatus status, final BasicListener listener) {
        firestore.collection("orders").document(orderId)
                .update("status", status.name())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());
                    }
                });
    }

}
