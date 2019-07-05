package com.marknkamau.justjava.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideKeyboard() {
    this.currentFocus?.let {
        val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun AppCompatActivity.showKeyboard() {
    val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}
