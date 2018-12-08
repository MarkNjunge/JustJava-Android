package com.marknjunge.core.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.marknjunge.core.model.*
import java.util.*

class StaffDatabaseImpl : StaffDatabaseService {
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun getOrders(listener: StaffDatabaseService.OrdersListener) {
        firestore.collection("orders")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val orders = ArrayList<Order>()
                    querySnapshot?.documents?.forEach {
                        val date = it.getTimestamp(DatabaseKeys.Order.date)!!.toDate()
                        it.data?.let { data ->
                            val order = Order(
                                    data[DatabaseKeys.Order.orderId] as String,
                                    data[DatabaseKeys.Order.customerId] as String,
                                    (data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                                    (data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                                    data[DatabaseKeys.Order.address] as String,
                                    data[DatabaseKeys.Order.comments] as String,
                                    OrderStatus.valueOf(data[DatabaseKeys.Order.status] as String),
                                    date,
                                    data[DatabaseKeys.Order.paymentMethod] as String,
                                    data[DatabaseKeys.Order.paymentStatus] as String

                            )
                            orders.add(order)
                        }
                    }

                    listener.onSuccess(orders)
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting orders")
                }
    }

    override fun getOrderItems(orderId: String, listener: StaffDatabaseService.OrderItemsListener) {
        firestore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val items = ArrayList<OrderItem>()
                    val documents = querySnapshot.documents

                    for (document in documents) {
                        document.data?.let {
                            val orderItem = OrderItem(
                                    0,
                                    it[DatabaseKeys.OrderItem.itemName] as String,
                                    (it[DatabaseKeys.OrderItem.itemQty] as Long).toInt(),
                                    it[DatabaseKeys.OrderItem.itemCinnamon] as Boolean,
                                    it[DatabaseKeys.OrderItem.itemChoc] as Boolean,
                                    it[DatabaseKeys.OrderItem.itemMarshmallow] as Boolean,
                                    (it[DatabaseKeys.OrderItem.itemPrice] as Long).toInt()
                            )
                            items.add(orderItem)
                        }
                    }

                    listener.onSuccess(items)
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting order items")
                }
    }

    override fun updateOrderStatus(orderId: String, status: OrderStatus, listener: WriteListener) {
        firestore.collection("orders").document(orderId)
                .update(DatabaseKeys.Order.status, status.name)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener { e ->
                    listener.onError(e.message ?: "Error updating order status")
                }
    }

    override fun getCustomerDetails(userId: String, listener: StaffDatabaseService.UserListener) {
        firestore.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { documentSnapshot ->
                        val user = UserDetails(
                                documentSnapshot[DatabaseKeys.User.userId] as String,
                                documentSnapshot[DatabaseKeys.User.name] as String,
                                documentSnapshot[DatabaseKeys.User.address] as String,
                                documentSnapshot[DatabaseKeys.User.email] as String,
                                documentSnapshot[DatabaseKeys.User.phone] as String)

                        listener.onSuccess(user)
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting user")
                }
    }

    override fun getPayments(listener: StaffDatabaseService.PaymentsListener) {
        firestore.collection("payments")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val payments = mutableListOf<Payment>()
                    querySnapshot.forEach { documentSnapshot ->
                        val payment = Payment(
                                documentSnapshot[DatabaseKeys.Payment.checkoutRequestId] as String,
                                documentSnapshot[DatabaseKeys.Payment.customerId] as String,
                                documentSnapshot[DatabaseKeys.Payment.date] as Long,
                                documentSnapshot[DatabaseKeys.Payment.merchantRequestId] as String,
                                documentSnapshot[DatabaseKeys.Payment.orderId] as String,
                                documentSnapshot[DatabaseKeys.Payment.resultDesc] as String,
                                documentSnapshot[DatabaseKeys.Payment.status] as String,
                                documentSnapshot[DatabaseKeys.Payment.mpesaReceiptNumber] as String?,
                                (documentSnapshot[DatabaseKeys.Payment.phoneNumber] as Long?).toString()
                        )
                        payments.add(payment)
                    }

                    listener.onSuccess(payments)
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting payments")
                }
    }

}