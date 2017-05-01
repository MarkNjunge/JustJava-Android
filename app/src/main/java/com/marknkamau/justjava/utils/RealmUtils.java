package com.marknkamau.justjava.utils;

import com.marknkamau.justjava.models.CartItem;

import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class RealmUtils {
    private Realm realm;
    private RealmResults<CartItem> allCartItems;

    public RealmUtils() {
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

    public void saveNewItem(final CartItem cartItem) {
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
    }

    public void deleteAllItems() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public int getTotalCost() {
        return realm.where(CartItem.class).sum("itemPrice").intValue();
    }

    public void deleteSingleItem(final CartItem item) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.deleteFromRealm();
            }
        });
    }

    public void saveEdit(final CartItem item) {
        Timber.i("Changing...");
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }

}
