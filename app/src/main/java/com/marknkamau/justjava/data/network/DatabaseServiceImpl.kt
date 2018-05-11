package com.marknkamau.justjava.data.network

import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.models.*
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
                .addOnSuccessListener {
                    listener.onSuccess()
                }
                .addOnFailureListener { exception ->
                    listener.onError(exception.message ?: "Error saving user details")
                }

    }

    override fun updateUserDetails(id: String, name: String, phone: String, address: String, listener: DatabaseService.WriteListener) {
        fireStore.collection("users")
                .document(id)
                .update(
                        "name", name,
                        "phone", phone,
                        "address", address
                )
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
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
                .addOnFailureListener { exception ->
                    listener.onError(exception.message ?: "Error getting user details")
                }
    }

    override fun placeNewOrder(userId: String?, order: Order, cartItems: List<CartItem>, listener: DatabaseService.WriteListener) {
        val orderRef = fireStore.collection("orders").document(order.orderId)
        val itemsRef = fireStore.collection("orderItems")

        val orderMap = mutableMapOf<String, Any>()
        orderMap["orderId"] = order.orderId
        orderMap["customerId"] = order.customerId
        orderMap["address"] = order.deliveryAddress
        orderMap["itemsCount"] = order.itemsCount
        orderMap["totalPrice"] = order.totalPrice
        orderMap["status"] = OrderStatus.PENDING.toString()
        orderMap["comments"] = order.additionalComments
        orderMap["date"] = FieldValue.serverTimestamp()

        FirebaseInstanceId.getInstance().token?.let {
            orderMap.put("fcmToken", it)
        }

        fireStore.runTransaction { }

        fireStore
                .runTransaction { transaction ->
                    transaction.set(orderRef, orderMap)
                    for (i in cartItems.indices) {
                        val item = cartItems[i]
                        val itemsMap = mutableMapOf<String, Any>()

                        itemsMap["orderId"] = order.orderId
                        itemsMap["itemName"] = item.itemName
                        itemsMap["itemQty"] = item.itemQty
                        itemsMap["itemCinnamon"] = item.itemCinnamon
                        itemsMap["itemChoc"] = item.itemChoc
                        itemsMap["itemMarshmallow"] = item.itemMarshmallow
                        itemsMap["itemPrice"] = item.itemPrice

                        val reference = itemsRef.document("${order.orderId}-$i")
                        transaction.set(reference, itemsMap)
                    }
                }
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener {
                    Timber.e(it)
                    listener.onError(it.message ?: "Error placing order")
                }
    }

    override fun getPreviousOrders(userId: String, listener: DatabaseService.PreviousOrdersListener) {
        fireStore.collection("orders")
                .whereEqualTo("customerId", userId)
                .get()
                .addOnSuccessListener {
                    val orders = mutableListOf<PreviousOrder>()
                    it.forEach { snapshot ->
                        val previousOrder = PreviousOrder(
                                snapshot.data["orderId"] as String,
                                (snapshot.data["itemsCount"] as Long).toInt(),
                                snapshot.data["address"] as String,
                                snapshot.data["date"] as Date,
                                (snapshot.data["totalPrice"] as Long).toInt(),
                                snapshot.data["status"] as String
                        )
                        orders.add(previousOrder)
                    }
                    listener.onSuccess(orders)
                }
                .addOnFailureListener {
                    Timber.e(it)
                    listener.onError(it.message ?: "Error getting previous orders")
                }

    }

    override fun getOrder(orderId: String, listener: DatabaseService.OrderListener) {

    }

}
