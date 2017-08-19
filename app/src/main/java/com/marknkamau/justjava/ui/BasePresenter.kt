package com.marknkamau.justjava.ui

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter{
    val disposables = CompositeDisposable()

    fun unSubscribe(){
        disposables.clear()
    }
}
