package com.marknkamau.justjavastaff.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus

import java.util.ArrayList
import java.util.Date

import timber.log.Timber

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class OrdersRepositoryImpl : OrdersRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

        firestore.firestoreSettings = settings
    }

    override fun getOrders(listener: OrdersRepository.OrdersListener) {
        val registration = firestore.collection("orders")
                .addSnapshotListener { querySnapshot, e ->
                    if (e != null) {
                        Timber.e(e)
                        listener.onError(e.message ?: "Error getting orders")
                    } else {
                        val orders = ArrayList<Order>()
                        val documentSnapshots = querySnapshot?.documents

                        documentSnapshots?.let {

                            for (document in documentSnapshots) {
                                val data = document.data
                                data?.let {

                                    val order = Order(
                                            data["orderId"] as String,
                                            data["customerName"] as String,
                                            data["customerPhone"] as String,
                                            data["deliveryAddress"] as String,
                                            data["additionalComments"] as String,
                                            data["status"] as String,
                                            data["timestampNow"] as Date,
                                            (data["totalPrice"] as Long).toInt(),
                                            (data["itemsCount"] as Long).toInt()
                                    )
                                    orders.add(order)
                                }
                            }

                            listener.onSuccess(orders)
                        }
                    }
                }
    }

    override fun getOrderItems(orderId: String, listener: OrdersRepository.OrderItemsListener) {
        firestore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val items = ArrayList<OrderItem>()
                    val documents = querySnapshot.documents

                    for (document in documents) {
                        val data = document.data
                        data?.let {
                            val orderItem = OrderItem(
                                    data["itemName"].toString(),
                                    (data["itemQty"] as Long).toInt(),
                                    data["itemCinnamon"] as Boolean,
                                    data["itemChoc"] as Boolean,
                                    data["itemMarshmallow"] as Boolean,
                                    (data["itemPrice"] as Long).toInt()
                            )
                            items.add(orderItem)
                        }
                    }

                    listener.onSuccess(items)
                }
                .addOnFailureListener { e ->
                    Timber.e(e)
                    listener.onError(e.message ?: "Error getting order items")
                }
    }

    override fun updateOderStatus(orderId: String, status: OrderStatus, listener: OrdersRepository.BasicListener) {
        firestore.collection("orders").document(orderId)
                .update("status", status.name)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener { e ->
                    listener.onError(e.message ?: "Error updating order status")
                }
    }

}
