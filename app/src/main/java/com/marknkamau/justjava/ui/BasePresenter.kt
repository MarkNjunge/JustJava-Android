package com.marknkamau.justjava.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BasePresenter(mainDispatcher: CoroutineDispatcher) {
    private val job = Job()
    protected val uiScope = CoroutineScope(job + mainDispatcher)

    fun unSubscribe() {
        job.cancel()
    }
}
