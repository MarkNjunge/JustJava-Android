package com.marknkamau.justjava.utils

import com.marknkamau.justjava.models.CartItem

import io.realm.Realm
import io.realm.RealmResults
import timber.log.Timber

class RealmUtils {
    private val realm: Realm = Realm.getDefaultInstance()
    private lateinit var allCartItems: RealmResults<CartItem>

    fun getAllCartItems(): RealmResults<CartItem> {
        realm.executeTransaction { realm -> allCartItems = realm.where(CartItem::class.java).findAll() }
        return allCartItems
    }

    fun saveNewItem(cartItem: CartItem) {
        realm.executeTransaction { realm ->
            val current = realm.where(CartItem::class.java).max("itemID")
            val nextID: Int
            if (current == null) {
                nextID = 1
            } else {
                nextID = current.toInt() + 1
            }
            cartItem.itemID = nextID

            realm.copyToRealm(cartItem)
        }
    }

    fun deleteAllItems() {
        realm.executeTransaction { realm -> realm.deleteAll() }
    }

    val totalPrice: Int
        get() = realm.where(CartItem::class.java).sum("itemPrice").toInt()

    fun deleteSingleItem(item: CartItem) {
        realm.executeTransaction { item.deleteFromRealm() }
    }

    fun saveEdit(item: CartItem) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(item) }
    }

}
