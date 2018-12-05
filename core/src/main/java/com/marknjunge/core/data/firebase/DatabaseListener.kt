package com.marknjunge.core.data.firebase

interface DatabaseListener {
    fun onError(reason: String)
}

interface WriteListener : DatabaseListener {
    fun onSuccess()
}