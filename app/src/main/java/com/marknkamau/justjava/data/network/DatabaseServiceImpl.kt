package com.marknkamau.justjava.data.network

import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.models.*
import timber.log.Timber

class DatabaseServiceImpl : DatabaseService {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()
    private var dbRootRef: DatabaseReference

    private lateinit var previousOrders: MutableList<PreviousOrder>

    init {
        fireStore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        database.setPersistenceEnabled(true)
        dbRootRef = database.reference
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
                .addOnFailureListener { listener.onError(it.message ?: "Error updating user details") }
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
        val orderRef = fireStore.collection("orders").document()
        val id = orderRef.id
        val itemsRef = fireStore.collection("orderItems")

        val orderMap = mutableMapOf<String, Any>()
        orderMap.put("orderId", id)
        orderMap.put("customerName", order.customerName)
        orderMap.put("customerPhone", order.customerPhone)
        orderMap.put("deliveryAddress", order.deliveryAddress)
        orderMap.put("itemsCount", order.itemsCount)
        orderMap.put("totalPrice", order.totalPrice)
        orderMap.put("status", OrderStatus.PENDING.toString())
        orderMap.put("additionalComments", order.additionalComments)
        orderMap.put("timestamp", FieldValue.serverTimestamp())

        userId?.let { orderMap.put("user", it) }

        fireStore
                .runTransaction {
                    it.set(orderRef, orderMap)
                    for (i in cartItems.indices){
                        val item = cartItems[i]
                        val itemsMap = mutableMapOf<String, Any>()

                        itemsMap.put("orderId", id)
                        itemsMap.put("itemName", item.itemName)
                        itemsMap.put("itemQty", item.itemQty)
                        itemsMap.put("itemCinnamon", item.itemCinnamon)
                        itemsMap.put("itemChoc", item.itemChoc)
                        itemsMap.put("itemMarshmallow", item.itemMarshmallow)
                        itemsMap.put("itemPrice", item.itemPrice)

                        val reference = itemsRef.document()
                        it.set(reference, itemsMap)
                    }
                }
                .addOnSuccessListener { listener.onSuccess()}
                .addOnFailureListener {
                    Timber.e(it)
                    listener.onError(it.message ?: "Error placing order")
                }
    }

    override fun getPreviousOrders(listener: DatabaseService.PreviousOrdersListener) {
        previousOrders = mutableListOf()
        dbRootRef.child("userOrders/" + AuthenticationServiceImpl.getCurrentUser()?.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.childrenCount.toInt() == 0) {
                            listener.noValuesPresent()
                        } else {
                            for (snapshot in dataSnapshot.children) {
                                getOrder(snapshot.value as String, object : DatabaseService.OrderListener {
                                    override fun taskSuccessful(deliveryAddress: String, timestamp: String, totalPrice: String, orderStatus: String) {
                                        previousOrders.add(PreviousOrder(deliveryAddress, timestamp, totalPrice, orderStatus))
                                        listener.taskSuccessful(previousOrders)
                                    }

                                    override fun onError(reason: String) {
                                        listener.onError(reason)
                                    }

                                })
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        listener.onError(databaseError.message)
                    }
                })
    }

    override fun getOrder(orderId: String, listener: DatabaseService.OrderListener) {
        val order = dbRootRef.child("allOrders/$orderId")
        order.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener.taskSuccessful(
                        dataSnapshot.child("deliveryAddress").value.toString(),
                        dataSnapshot.child("timestamp").value.toString(),
                        dataSnapshot.child("totalPrice").value.toString(),
                        dataSnapshot.child("orderStatus").value.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.onError(databaseError.message)
            }

        })
    }

}
