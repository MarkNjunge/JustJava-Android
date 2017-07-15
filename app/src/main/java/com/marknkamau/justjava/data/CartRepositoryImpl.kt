package com.marknkamau.justjava.data

import com.marknkamau.justjava.models.CartItem

import io.realm.Realm
import io.realm.RealmResults

object CartRepositoryImpl : CartRepository {
    private val realm: Realm = Realm.getDefaultInstance()
    private lateinit var allCartItems: RealmResults<CartItem>

    override fun getAllCartItems(): RealmResults<CartItem> {
        realm.executeTransaction { realm -> allCartItems = realm.where(CartItem::class.java).findAll() }
        return allCartItems
    }

    override fun getTotalPrice(): Int {
        return realm.where(CartItem::class.java).sum("itemPrice").toInt()
    }

    override fun saveNewItem(cartItem: CartItem) {
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

    override fun deleteAllItems() {
        realm.executeTransaction { realm -> realm.deleteAll() }
    }

    override fun deleteSingleItem(item: CartItem) {
        realm.executeTransaction { item.deleteFromRealm() }
    }

    override fun saveEdit(item: CartItem) {
        realm.executeTransaction { realm -> realm.copyToRealmOrUpdate(item) }
    }

}
