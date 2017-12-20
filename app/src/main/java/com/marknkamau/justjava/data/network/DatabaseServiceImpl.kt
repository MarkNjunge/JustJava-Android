package com.marknkamau.justjava.data.network

import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDetails

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

    override fun getUserDefaults(id:String, listener: DatabaseService.UserDetailsListener) {
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

    override fun placeNewOrder(order: Order, cartItems: List<CartItem>, listener: DatabaseService.WriteListener) {
        val orderRef = dbRootRef.child("allOrders").push()
        val key = orderRef.key

        orderRef.child("orderID").setValue(key)
        orderRef.child("customerName").setValue(order.customerName)
        orderRef.child("customerPhone").setValue(order.customerPhone)
        orderRef.child("itemsCount").setValue(order.itemsCount)
        orderRef.child("totalPrice").setValue(order.totalPrice)
        orderRef.child("orderStatus").setValue("Pending")
        orderRef.child("deliveryAddress").setValue(order.deliveryAddress)
        orderRef.child("additionalComments").setValue(order.additionalComments)
        orderRef.child("deviceToken").setValue(FirebaseInstanceId.getInstance().token)
        orderRef.child("timestamp").setValue(ServerValue.TIMESTAMP)

        val currentUser = AuthenticationServiceImpl.getCurrentUser()
        if (currentUser != null) {
            orderRef.child("user").setValue(currentUser.uid)
            val userOrdersRef = dbRootRef.child("userOrders/${currentUser.uid}")
            userOrdersRef.push().setValue(key)
        } else {
            orderRef.child("user").setValue("null")
        }

        val orderItemsRef = dbRootRef.child("orderItems").child(key)
        orderItemsRef.setValue(cartItems)
                .addOnSuccessListener { listener.onSuccess() }
                .addOnFailureListener { exception -> listener.onError(exception.message ?: "Error placing order") }
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
