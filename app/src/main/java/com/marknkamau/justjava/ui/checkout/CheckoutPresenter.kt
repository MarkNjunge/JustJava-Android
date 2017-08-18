package com.marknkamau.justjava.ui.checkout

import com.marknkamau.justjava.authentication.AuthenticationService
import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.Order
import com.marknkamau.justjava.network.DatabaseServiceImpl
import com.marknkamau.justjava.data.PreferencesRepository
import com.marknkamau.justjava.models.CartItemRoom
import com.marknkamau.justjava.network.DatabaseService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class CheckoutPresenter(private val activityView: CheckoutView, auth: AuthenticationService, preferences: PreferencesRepository, private val cart: CartDao) {
    private var getAllDisposable: Disposable? = null
    private var deleteAllDisposable: Disposable? = null
    private var resetIndexDisposable: Disposable? = null

    init {
        if (auth.isSignedIn()) {
            activityView.setDisplayToLoggedIn(auth.getCurrentUser()!!, preferences.getDefaults())
        } else {
            activityView.setDisplayToLoggedOut()
        }
    }

    fun placeOrder(order: Order) {
        activityView.showUploadBar()

        getAllDisposable = cart.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items: MutableList<CartItemRoom>? ->
                            getAllDisposable?.dispose()
                            placeOrderInternal(items!!, order)
                        },
                        { t: Throwable? ->
                            Timber.e(t)
                        }
                )

    }

    private fun placeOrderInternal(items: MutableList<CartItemRoom>, order: Order) {
        val itemsCount = items.size
        var total = 0
        items.forEach { item -> total += item.itemPrice }

        order.itemsCount = itemsCount
        order.totalPrice = total

        DatabaseServiceImpl.placeNewOrder(order, items, object : DatabaseService.UploadListener {
            override fun taskSuccessful() {

                val deleteAll = Completable.fromCallable { cart.deleteAll() }
                val resetIndex = Completable.fromCallable { cart.resetIndex() }

                deleteAllDisposable = deleteAll.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    resetIndexDisposable = resetIndex.subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                deleteAllDisposable?.dispose()
                                                activityView.hideUploadBar()
                                                activityView.showMessage("Order placed")
                                                activityView.finishActivity()
                                            })
                                }
                        )


            }

            override fun taskFailed(reason: String?) {
                activityView.hideUploadBar()
                activityView.showMessage(reason)
            }
        })

    }
}
