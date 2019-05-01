package com.marknjunge.core.data.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.model.*
import java.util.*

internal class ClientDatabaseImpl : ClientDatabaseService {
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun saveUserDetails(userDetails: UserDetails, listener: WriteListener) {
        firestore.collection("users")
                .document(userDetails.id)
                .set(userDetails)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error saving user details")
                }
    }

    override fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: WriteListener) {
        val userDetailsMap = mapOf(
                DatabaseKeys.User.name to name,
                DatabaseKeys.User.phone to phone,
                DatabaseKeys.User.address to address
        )

        firestore.collection("users")
                .document(id)
                .update(userDetailsMap)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error updating user details")
                }
    }

    override fun updateUserFcmToken(userId: String, listener: WriteListener) {
        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener { instanceIdResult ->
                    val data = mapOf(
                            DatabaseKeys.User.fcmToken to instanceIdResult.token
                    )
                    firestore.collection("users")
                            .document(userId)
                            .update(data)
                            .addOnSuccessListener {
                                listener.onSuccess()
                            }
                            .addOnFailureListener {
                                listener.onError(it.message ?: "Unable tp update user token")
                            }
                }
                .addOnFailureListener { listener.onError(it.message ?: "Unable to retrieve user token") }
    }

    override fun getUserDefaults(id: String, listener: ClientDatabaseService.UserDetailsListener) {
        firestore.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener {
                    val userDetails = UserDetails(
                            it[DatabaseKeys.User.userId] as String,
                            it[DatabaseKeys.User.email] as String,
                            it[DatabaseKeys.User.name] as String,
                            it[DatabaseKeys.User.phone] as String,
                            it[DatabaseKeys.User.address] as String
                    )
                    listener.onSuccess(userDetails)
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting user details")
                }
    }

    override fun placeNewOrder(order: Order, orderItems: List<OrderItem>, listener: WriteListener) {
        val orderRef = firestore.collection("orders").document(order.orderId)
        val itemsRef = firestore.collection("orderItems")

        val orderMap = mutableMapOf<String, Any>()
        orderMap[DatabaseKeys.Order.orderId] = order.orderId
        orderMap[DatabaseKeys.Order.customerId] = order.customerId
        orderMap[DatabaseKeys.Order.address] = order.deliveryAddress
        orderMap[DatabaseKeys.Order.itemsCount] = order.itemsCount
        orderMap[DatabaseKeys.Order.totalPrice] = order.totalPrice
        orderMap[DatabaseKeys.Order.status] = order.status.name
        orderMap[DatabaseKeys.Order.comments] = order.additionalComments
        orderMap[DatabaseKeys.Order.date] = FieldValue.serverTimestamp()
        orderMap[DatabaseKeys.Order.paymentMethod] = order.paymentMethod
        orderMap[DatabaseKeys.Order.paymentStatus] = order.paymentStatus

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful) {
                orderMap[DatabaseKeys.Order.fcmToken] = it.result!!.token
            }
            placeOrder(orderRef, orderMap, orderItems, order, itemsRef, listener)
        }
    }

    override fun getPreviousOrders(userId: String, listener: ClientDatabaseService.PreviousOrdersListener) {
        firestore.collection("orders")
                .whereEqualTo("customerId", userId)
                .get()
                .addOnSuccessListener {
                    val orders = mutableListOf<Order>()
                    it.forEach { snapshot ->
                        val order = Order(
                                snapshot.data[DatabaseKeys.Order.orderId] as String,
                                snapshot.data[DatabaseKeys.Order.customerId] as String,
                                (snapshot.data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                                (snapshot.data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                                snapshot.data[DatabaseKeys.Order.address] as String,
                                snapshot.data[DatabaseKeys.Order.comments] as String,
                                OrderStatus.valueOf(snapshot.data[DatabaseKeys.Order.status] as String),
                                snapshot.getTimestamp(DatabaseKeys.Order.date)!!.toDate(),
                                snapshot.data[DatabaseKeys.Order.paymentMethod] as String?
                                        ?: "cash",
                                snapshot.data[DatabaseKeys.Order.paymentStatus] as String?
                                        ?: "unpaid"

                        )
                        orders.add(order)
                    }
                    listener.onSuccess(orders)
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting previous orders")
                }
    }

    override fun getOrderItems(orderId: String, listener: ClientDatabaseService.OrderItemsListener) {
        firestore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val items = ArrayList<OrderItem>()
                    val documents = querySnapshot.documents

                    for (document in documents) {
                        val data = document.data
                        data?.let {
                            val orderItem = OrderItem(
                                    0,
                                    data[DatabaseKeys.OrderItem.itemName].toString(),
                                    (data[DatabaseKeys.OrderItem.itemQty] as Long).toInt(),
                                    data[DatabaseKeys.OrderItem.itemCinnamon] as Boolean,
                                    data[DatabaseKeys.OrderItem.itemChoc] as Boolean,
                                    data[DatabaseKeys.OrderItem.itemMarshmallow] as Boolean,
                                    (data[DatabaseKeys.OrderItem.itemPrice] as Long).toInt()
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

    override fun savePaymentRequest(merchantRequestId: String, checkoutRequestId: String, orderId: String, customerId: String) {
        val map = mapOf(
                DatabaseKeys.Payment.checkoutRequestId to checkoutRequestId,
                DatabaseKeys.Payment.merchantRequestId to merchantRequestId,
                DatabaseKeys.Payment.orderId to orderId,
                DatabaseKeys.Payment.customerId to customerId,
                DatabaseKeys.Payment.status to "pending"
        )

        firestore.collection("payments")
                .document()
                .set(map)
                .addOnFailureListener {
                }
    }

    override fun getOrder(orderId: String, listener: ClientDatabaseService.OrderListener) {
        firestore.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.data?.let { data ->

                        val order = Order(
                                data[DatabaseKeys.Order.orderId] as String,
                                data[DatabaseKeys.Order.customerId] as String,
                                (data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                                (data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                                data[DatabaseKeys.Order.address] as String,
                                data[DatabaseKeys.Order.comments] as String,
                                OrderStatus.valueOf(data[DatabaseKeys.Order.status] as String),
                                snapshot.getTimestamp(DatabaseKeys.Order.date)!!.toDate(),
                                data[DatabaseKeys.Order.paymentMethod] as String? ?: "cash",
                                data[DatabaseKeys.Order.paymentStatus] as String? ?: "unpaid"

                        )

                        listener.onSuccess(order)
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error getting order items")
                }
    }

    private fun placeOrder(orderRef: DocumentReference, orderMap: MutableMap<String, Any>, orderItems: List<OrderItem>, order: Order, itemsRef: CollectionReference, listener: WriteListener) {
        firestore
                .runTransaction { transaction ->
                    transaction.set(orderRef, orderMap)
                    for (i in orderItems.indices) {
                        val item = orderItems[i]
                        val itemsMap = mutableMapOf<String, Any>()

                        itemsMap[DatabaseKeys.Order.orderId] = order.orderId
                        itemsMap[DatabaseKeys.OrderItem.itemName] = item.itemName
                        itemsMap[DatabaseKeys.OrderItem.itemQty] = item.itemQty
                        itemsMap[DatabaseKeys.OrderItem.itemCinnamon] = item.itemCinnamon
                        itemsMap[DatabaseKeys.OrderItem.itemChoc] = item.itemChoc
                        itemsMap[DatabaseKeys.OrderItem.itemMarshmallow] = item.itemMarshmallow
                        itemsMap[DatabaseKeys.OrderItem.itemPrice] = item.itemPrice
                        itemsMap[DatabaseKeys.OrderItem.itemPrice] = item.itemPrice
                        itemsMap[DatabaseKeys.OrderItem.itemPrice] = item.itemPrice

                        val reference = itemsRef.document("${order.orderId}-$i")
                        transaction.set(reference, itemsMap)
                    }
                }
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    listener.onError(it.message ?: "Error placing order")
                }
    }

}