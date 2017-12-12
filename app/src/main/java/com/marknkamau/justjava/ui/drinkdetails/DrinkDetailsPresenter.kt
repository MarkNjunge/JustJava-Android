package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItem
import com.marknkamau.justjava.ui.BasePresenter
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class DrinkDetailsPresenter(val activityView: DrinkDetailsView, val cart: CartDao) : BasePresenter() {

    fun addToCart(item: CartItem) {
        disposables.add(Completable.fromCallable { cart.addItem(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onComplete = {
                            activityView.displayMessage("Item added to cart")
                            activityView.finishActivity()
                        },
                        onError = { t: Throwable? ->
                            Timber.e(t)
                            activityView.displayMessage(t?.message)
                        }
                ))
    }
}
