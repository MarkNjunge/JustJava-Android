package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom
import com.marknkamau.justjava.ui.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class CartPresenter(private val activityView: CartView, private val cart: CartDao)
    : BasePresenter() {

    fun loadItems() {
        disposables.add(cart.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { items: MutableList<CartItemRoom>? ->
                            val size: Int = items?.size ?: 0
                            if (size > 0) {
                                activityView.displayCart(items)
                                var total = 0
                                items?.forEach { item -> total += item.itemPrice }
                                activityView.displayCartTotal(total)
                            } else {
                                activityView.displayEmptyCart()
                            }
                        },
                        onError = { t: Throwable? ->
                            Timber.e(t)
                            activityView.displayMessage(t?.message)
                        }
                ))
    }

    fun clearCart() {
        disposables.add(Completable.fromCallable { cart.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            activityView.displayEmptyCart()
                        },
                        onError = { t: Throwable? ->
                            Timber.e(t)
                            activityView.displayMessage(t?.message)
                        }
                ))
    }
}


