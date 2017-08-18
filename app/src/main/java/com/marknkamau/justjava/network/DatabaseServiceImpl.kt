package com.marknkamau.justjava.network

import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.authentication.AuthenticationServiceImpl
import com.marknkamau.justjava.models.*

object DatabaseServiceImpl : DatabaseService {
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private var dbRootRef: DatabaseReference

    private lateinit var previousOrders: MutableList<PreviousOrder>

    init {
        database.setPersistenceEnabled(true)
        dbRootRef = database.reference
    }

    override fun getUserDefaults(listener: DatabaseService.UserDetailsListener) {
        if (AuthenticationServiceImpl.isSignedIn()){

        val ref = dbRootRef.child("users/${AuthenticationServiceImpl.getCurrentUser()?.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener.taskSuccessful(
                        dataSnapshot.child("name").value.toString(),
                        dataSnapshot.child("phone").value.toString(),
                        dataSnapshot.child("defaultAddress").value.toString()
                )
            }

            override fun onCancelled(databaseError: DatabaseError) {
                listener.taskFailed(databaseError.message)
            }
        })
        }else{
            listener.taskFailed("No user is signed in")
        }
    }

    override fun placeNewOrder(order: Order, cartItems: List<CartItemRoom>, listener: DatabaseService.UploadListener) {
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
                .addOnSuccessListener { listener.taskSuccessful() }
                .addOnFailureListener { exception -> listener.taskFailed(exception.message) }
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

                                    override fun taskFailed(reason: String?) {
                                        listener.taskFailed(reason)
                                    }

                                })
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        listener.taskFailed(databaseError.message)
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
                listener.taskFailed(databaseError.message)
            }

        })
    }

    override fun setUserDefaults(userDefaults: UserDefaults, listener: DatabaseService.UploadListener) {
        val databaseReference = dbRootRef.child("users/${AuthenticationServiceImpl.getCurrentUser()?.uid}")
        databaseReference.setValue(userDefaults)
                .addOnSuccessListener { listener.taskSuccessful() }
                .addOnFailureListener { exception -> listener.taskFailed(exception.message) }

    }

}
