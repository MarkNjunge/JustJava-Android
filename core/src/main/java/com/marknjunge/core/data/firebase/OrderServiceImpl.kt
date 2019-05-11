package com.marknjunge.core.data.firebase

import com.google.firebase.firestore.*
import com.google.firebase.iid.FirebaseInstanceId
import com.marknjunge.core.model.DatabaseKeys
import com.marknjunge.core.model.Order
import com.marknjunge.core.model.OrderItem
import com.marknjunge.core.model.OrderStatus
import kotlinx.coroutines.tasks.await

internal class OrderServiceImpl(private val firestore: FirebaseFirestore) : OrderService {

    override suspend fun placeNewOrder(order: Order, orderItems: List<OrderItem>) {
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

        val instanceIdResult = FirebaseInstanceId.getInstance().instanceId.await()
        orderMap[DatabaseKeys.Order.fcmToken] = instanceIdResult.token

        placeOrder(orderRef, orderMap, orderItems, order, itemsRef)
    }

    override suspend fun getPreviousOrders(userId: String): List<Order> = getOrders(userId)

    override suspend fun getOrderItems(orderId: String): List<OrderItem> {
        val querySnapshot = firestore.collection("orderItems").whereEqualTo("orderId", orderId)
                .get()
                .await()

        return querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.mapToOrderItem()
        }
    }

    override suspend fun getOrder(orderId: String): Order {
        val snapshot = firestore.collection("orders")
                .document(orderId)
                .get()
                .await()

        return snapshot.mapToOrder() ?: throw Exception("There is no order with id $orderId")
    }

    override suspend fun getAllOrders(): List<Order> = getOrders()

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        firestore.collection("orders").document(orderId)
                .update(DatabaseKeys.Order.status, status.name)
                .await()
    }

    private suspend fun placeOrder(orderRef: DocumentReference, orderMap: MutableMap<String, Any>, orderItems: List<OrderItem>, order: Order, itemsRef: CollectionReference) {
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
                .await()

    }

    private suspend fun getOrders(userId: String? = null): List<Order> {
        val querySnapshot = if (userId == null) {
            firestore.collection("orders")
        } else {
            firestore.collection("orders").whereEqualTo("customerId", userId)
        }.get().await()

        return querySnapshot.mapNotNull { snapshot ->
            snapshot.mapToOrder()
        }
    }

    private fun QueryDocumentSnapshot.mapToOrder() =
            Order(
                    this.data[DatabaseKeys.Order.orderId] as String,
                    this.data[DatabaseKeys.Order.customerId] as String,
                    (this.data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                    (this.data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                    this.data[DatabaseKeys.Order.address] as String,
                    this.data[DatabaseKeys.Order.comments] as String,
                    OrderStatus.valueOf(this.data[DatabaseKeys.Order.status] as String),
                    this.getTimestamp(DatabaseKeys.Order.date)!!.toDate(),
                    this.data[DatabaseKeys.Order.paymentMethod] as String? ?: "cash",
                    this.data[DatabaseKeys.Order.paymentStatus] as String? ?: "unpaid"
            )

    private fun DocumentSnapshot.mapToOrder() = this.data?.let { data ->
        Order(
                data[DatabaseKeys.Order.orderId] as String,
                data[DatabaseKeys.Order.customerId] as String,
                (data[DatabaseKeys.Order.itemsCount] as Long).toInt(),
                (data[DatabaseKeys.Order.totalPrice] as Long).toInt(),
                data[DatabaseKeys.Order.address] as String,
                data[DatabaseKeys.Order.comments] as String,
                OrderStatus.valueOf(data[DatabaseKeys.Order.status] as String),
                this.getTimestamp(DatabaseKeys.Order.date)!!.toDate(),
                data[DatabaseKeys.Order.paymentMethod] as String? ?: "cash",
                data[DatabaseKeys.Order.paymentStatus] as String? ?: "unpaid"
        )
    }

    private fun DocumentSnapshot.mapToOrderItem() = this.data?.let { data ->
        OrderItem(
                0,
                data[DatabaseKeys.OrderItem.itemName].toString(),
                (data[DatabaseKeys.OrderItem.itemQty] as Long).toInt(),
                data[DatabaseKeys.OrderItem.itemCinnamon] as Boolean,
                data[DatabaseKeys.OrderItem.itemChoc] as Boolean,
                data[DatabaseKeys.OrderItem.itemMarshmallow] as Boolean,
                (data[DatabaseKeys.OrderItem.itemPrice] as Long).toInt()
        )
    }
}