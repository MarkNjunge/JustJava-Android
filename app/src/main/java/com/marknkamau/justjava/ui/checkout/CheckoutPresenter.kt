package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.local.CartDao
import com.marknkamau.justjava.data.local.PreferencesRepository
import com.marknkamau.justjava.data.network.DatabaseService
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.ui.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class CheckoutPresenter(private val activityView: CheckoutView,
                                 private val auth: AuthenticationService,
                                 private val preferences: PreferencesRepository,
                                 private val database: DatabaseService,
                                 private val cart: CartDao) : BasePresenter() {
    fun getSignInStatus() {
        if (auth.isSignedIn()) {
            activityView.setDisplayToLoggedIn(preferences.getUserDetails())
        } else {
            activityView.setDisplayToLoggedOut()
        }
    }

    fun placeOrder(order: Order) {
        activityView.showUploadBar()

        disposables.add(cart.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = { items: MutableList<CartItem> ->
                            placeOrderInternal(items, order)
                        },
                        onError = { t: Throwable? ->
                            Timber.e(t)
                        }
                ))

    }

    private fun placeOrderInternal(items: MutableList<CartItem>, order: Order) {
        val itemsCount = items.size
        var total = 0
        items.forEach { item -> total += item.itemPrice }

        order.itemsCount = itemsCount
        order.totalPrice = total

        // TODO change getCurrentUser to not return FirebaseUser
        val userId = auth.getCurrentUser()?.uid

        database.placeNewOrder(userId, order, items, object : DatabaseService.WriteListener {
            override fun onSuccess() {
                disposables.add(Completable.fromCallable { cart.deleteAll() }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                                onComplete = {
                                    activityView.hideUploadBar()
                                    activityView.displayMessage("Order placed")
                                    activityView.finishActivity()
                                },
                                onError = { t: Throwable? ->
                                    Timber.e(t)
                                    activityView.hideUploadBar()
                                    activityView.displayMessage(t?.message)
                                }
                        ))

            }

            override fun onError(reason: String) {
                activityView.hideUploadBar()
                activityView.displayMessage(reason)
            }
        })

    }
}
