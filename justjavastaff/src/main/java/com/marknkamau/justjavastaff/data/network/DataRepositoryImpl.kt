package com.marknkamau.justjavastaff.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.marknkamau.justjavastaff.models.Order
import com.marknkamau.justjavastaff.models.OrderItem
import com.marknkamau.justjavastaff.models.OrderStatus
import com.marknkamau.justjavastaff.models.User

import java.util.ArrayList
import java.util.Date

import timber.log.Timber

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class DataRepositoryImpl : DataRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

        firestore.firestoreSettings = settings
    }

    override fun getOrders(listener: DataRepository.OrdersListener) {
        firestore.collection("orders")
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
                                            data[DatabaseKeys.Order.orderId] as String,
                                            data[DatabaseKeys.Order.customerId] as String,
                                            (data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                                            (data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                                            data[DatabaseKeys.Order.address] as String,
                                            data[DatabaseKeys.Order.comments] as String,
                                            OrderStatus.valueOf(data[DatabaseKeys.Order.status] as String),
                                            data[DatabaseKeys.Order.date] as Date
                                    )

                                    orders.add(order)
                                }
                            }

                            listener.onSuccess(orders)
                        }
                    }
                }
    }

    override fun getOrderItems(orderId: String, listener: DataRepository.OrderItemsListener) {
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

    override fun updateOderStatus(orderId: String, status: OrderStatus, listener: DataRepository.BasicListener) {
        firestore.collection("orders").document(orderId)
                .update("status", status.name)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener { e ->
                    listener.onError(e.message ?: "Error updating order status")
                }
    }

    override fun getCustomerDetails(userId: String, listener: DataRepository.UserListener) {
        firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { documentSnapshot ->
                        val user = User(
                                documentSnapshot[DatabaseKeys.User.id] as String,
                                documentSnapshot[DatabaseKeys.User.name] as String,
                                documentSnapshot[DatabaseKeys.User.address] as String,
                                documentSnapshot[DatabaseKeys.User.email] as String,
                                documentSnapshot[DatabaseKeys.User.phone] as String)

                        listener.onSuccess(user)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(exception)
                    listener.onError(exception.localizedMessage)
                }
    }

}
