package com.marknkamau.justjava.ui

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BasePresenter(mainDispatcher: CoroutineDispatcher) {
    private val job = SupervisorJob()
    protected val uiScope = CoroutineScope(job + mainDispatcher)

    fun cancel() {
        job.cancel()
    }
}
