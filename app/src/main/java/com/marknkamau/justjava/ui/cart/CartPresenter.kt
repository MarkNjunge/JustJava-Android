package com.marknkamau.justjava.ui.cart

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class CartPresenter(private val activityView: CartView, private val cart: CartDao) {
    private var getAllDisposable: Disposable? = null
    private var clearCartDisposable: Disposable? = null

    fun loadItems() {
        getAllDisposable = cart.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items: MutableList<CartItemRoom>? ->
                            getAllDisposable?.dispose()
                            Timber.d(items?.toString())
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
                        { t: Throwable? ->
                            Timber.e(t)
                        }
                )
    }

    fun clearCart() {
        clearCartDisposable = Single.fromCallable { cart.deleteAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ ->
                            clearCartDisposable?.dispose()
                            activityView.displayEmptyCart()
                        },
                        { t ->
                            Timber.e(t)
                        }
                )
        activityView.displayEmptyCart()
    }

}


