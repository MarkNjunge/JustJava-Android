package com.marknkamau.justjava.utils;

import android.content.Context;

import com.marknkamau.justjava.models.CartItem;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmUtils {
    private Realm realm;
    private RealmResults<CartItem> allCartItems;

    public RealmUtils(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<CartItem> getAllCartItems() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                allCartItems = realm.where(CartItem.class).findAll();
            }
        });
        return allCartItems;
    }

    public void saveNewItem(final CartItem cartItem, final RealmActionCompleted actionCompleted) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number current = realm.where(CartItem.class).max("itemID");
                int nextID;
                if (current == null) {
                    nextID = 1;
                } else {
                    nextID = current.intValue() + 1;
                }
                cartItem.setItemID(nextID);

                realm.copyToRealm(cartItem);
            }
        });
        actionCompleted.actionCompleted();
    }

    public void deleteAllItems(final RealmActionCompleted actionCompleted) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
        actionCompleted.actionCompleted();
    }

    public int getTotalCost() {
        return realm.where(CartItem.class).sum("itemPrice").intValue();
    }

    public void deleteSingleItem(final CartItem item, final RealmActionCompleted actionCompleted) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
        actionCompleted.actionCompleted();
    }

    public void saveEdit(final CartItem item, final RealmActionCompleted actionCompleted) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
        actionCompleted.actionCompleted();
    }

    public interface RealmActionCompleted {
        void actionCompleted();
    }
}
