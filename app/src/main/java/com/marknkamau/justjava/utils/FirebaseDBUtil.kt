package com.marknkamau.justjava.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.models.PreviousOrder
import com.marknkamau.justjava.models.UserDefaults
import timber.log.Timber

object FirebaseDBUtil {
    private var database: FirebaseDatabase? = null

    private lateinit var previousOrders: MutableList<PreviousOrder>

    init {
        if (database == null) {
            database = FirebaseDatabase.getInstance()
            database!!.setPersistenceEnabled(true)
        }
    }

    fun getUserDefaults(listener: UserDetailsListener) {
        val database = database!!.reference.child("users/${FirebaseAuthUtils.currentUser!!.uid}")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
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
    }

    fun placeNewOrder(order: Order, cartItems: List<CartItem>, listener: UploadListener) {
        val orderRef = database!!.reference.child("allOrders").push()
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

        val currentUser = FirebaseAuthUtils.currentUser
        if (currentUser != null) {
            orderRef.child("user").setValue(currentUser.uid)
            val userOrdersRef = database!!.reference.child("userOrders/${currentUser.uid}")
            userOrdersRef.push().setValue(key)
        } else {
            orderRef.child("user").setValue("null")
        }

        val orderItemsRef = database!!.reference.child("orderItems").child(key)
        orderItemsRef.setValue(cartItems)
                .addOnSuccessListener { listener.taskSuccessful() }
                .addOnFailureListener { exception -> listener.taskFailed(exception.message) }
    }

    fun getPreviousOrders(listener: PreviousOrdersListener) {
        previousOrders = mutableListOf()
        database!!.reference.child("userOrders/" + FirebaseAuthUtils.currentUser!!.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.childrenCount.toInt() == 0) {
                            listener.noValuesPresent()
                        } else {
                            for (snapshot in dataSnapshot.children) {
                                Timber.d(snapshot.value as String)
                                getOrder(snapshot.value as String, object : OrderListener {
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

    fun getOrder(orderId: String, listener: OrderListener) {
        val order = database!!.reference.child("allOrders/$orderId")
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

    fun setUserDefaults(userDefaults: UserDefaults, listener: UploadListener) {
        val databaseReference = database!!.reference.child("users/${FirebaseAuthUtils.currentUser!!.uid}")
        databaseReference.setValue(userDefaults)
                .addOnSuccessListener { listener.taskSuccessful() }
                .addOnFailureListener { exception -> listener.taskFailed(exception.message) }

    }

    interface DatabaseListener {
        fun taskFailed(reason: String?)
    }

    interface UserDetailsListener : DatabaseListener {
        fun taskSuccessful(name: String, phone: String, deliveryAddress: String)
    }

    interface UploadListener : DatabaseListener {
        fun taskSuccessful()
    }

    interface PreviousOrdersListener : DatabaseListener {
        fun taskSuccessful(previousOrders: MutableList<PreviousOrder>)

        fun noValuesPresent()
    }

    interface OrderListener : DatabaseListener {
        fun taskSuccessful(deliveryAddress: String, timestamp: String, totalPrice: String, orderStatus: String)
    }

}
