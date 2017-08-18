package com.marknkamau.justjava.ui.drinkdetails

import com.marknkamau.justjava.data.CartDao
import com.marknkamau.justjava.models.CartItemRoom
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class DrinkDetailsPresenter(val activityView: DrinkDetailsView, val cart: CartDao) {

    fun addToCart(item: CartItemRoom) {
        Single.fromCallable { cart.addItem(item) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { _ ->
                            activityView.displayMessage("Item added to cart")
                            activityView.finishActivity()
                        },
                        { t ->
                            Timber.e(t)
                        }
                )
    }
}
