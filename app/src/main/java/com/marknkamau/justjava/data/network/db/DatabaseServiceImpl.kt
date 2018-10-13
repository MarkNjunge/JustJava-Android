package com.marknkamau.justjava.data.network.db

import com.crashlytics.android.Crashlytics
import com.google.firebase.firestore.*
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.data.models.Order
import com.marknkamau.justjava.data.models.OrderItem
import com.marknkamau.justjava.data.models.OrderStatus
import com.marknkamau.justjava.data.models.UserDetails
import timber.log.Timber
import java.util.*

class DatabaseServiceImpl : DatabaseService {
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
    }

    override fun saveUserDetails(userDetails: UserDetails, listener: DatabaseService.WriteListener) {
        fireStore.collection("users")
                .document(userDetails.id)
                .set(userDetails)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error saving user details")
                }

    }

    override fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: DatabaseService.WriteListener) {
        val userDetailsMap = mapOf(
                "name" to name,
                "phone" to phone,
                "address" to address
        )

        fireStore.collection("users")
                .document(id)
                .update(userDetailsMap)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error updating user details")
                }
    }

    override fun getUserDefaults(id: String, listener: DatabaseService.UserDetailsListener) {
        fireStore.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener {
                    val userDetails = UserDetails(
                            it["id"] as String,
                            it["email"] as String,
                            it["name"] as String,
                            it["phone"] as String,
                            it["address"] as String
                    )
                    listener.onSuccess(userDetails)
                }
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error getting user details")
                }
    }

    override fun placeNewOrder(order: Order, orderItems: List<OrderItem>, listener: DatabaseService.WriteListener) {
        val orderRef = fireStore.collection("orders").document(order.orderId)
        val itemsRef = fireStore.collection("orderItems")

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
                orderMap["fcmToken"] = it.result.token
            }
            placeOrder(orderRef, orderMap, orderItems, order, itemsRef, listener)
        }

    }

    private fun placeOrder(orderRef: DocumentReference, orderMap: MutableMap<String, Any>, orderItems: List<OrderItem>, order: Order, itemsRef: CollectionReference, listener: DatabaseService.WriteListener) {
        fireStore
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
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error placing order")
                }
    }

    override fun getPreviousOrders(userId: String, listener: DatabaseService.PreviousOrdersListener) {
        fireStore.collection("orders")
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
                                snapshot.data[DatabaseKeys.Order.date] as Date,
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
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error getting previous orders")
                }

    }

    override fun getOrderItems(orderId: String, listener: DatabaseService.OrderItemsListener) {
        fireStore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val items = ArrayList<OrderItem>()
                    val documents = querySnapshot.documents

                    for (document in documents) {
                        val data = document.data
                        data?.let {
                            val orderItem = OrderItem(
                                    0,
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
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error getting order items")
                }
    }

    override fun savePaymentRequest(merchantRequestId: String, checkoutRequestId: String, orderId: String, customerId: String) {
        val map = mapOf(
                DatabaseKeys.Payment.CHECKOUT_REQUEST_ID to checkoutRequestId,
                DatabaseKeys.Payment.MERCHANT_REQUEST_ID to merchantRequestId,
                DatabaseKeys.Payment.ORDER_ID to orderId,
                DatabaseKeys.Payment.CUSTOMER_ID to customerId,
                DatabaseKeys.Payment.STATUS to "pending"
        )

        fireStore.collection("payments")
                .document()
                .set(map)
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                }
    }

    override fun getOrder(orderId: String, listener: DatabaseService.OrderListener) {
        fireStore.collection("orders").document(orderId)
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
                                data[DatabaseKeys.Order.date] as Date,
                                data[DatabaseKeys.Order.paymentMethod] as String? ?: "cash",
                                data[DatabaseKeys.Order.paymentStatus] as String? ?: "unpaid"

                        )

                        listener.onSuccess(order)
                    }
                }
                .addOnFailureListener {
                    Timber.e(it)
                    Crashlytics.logException(it)
                    listener.onError(it.message ?: "Error getting order items")
                }
    }

}
