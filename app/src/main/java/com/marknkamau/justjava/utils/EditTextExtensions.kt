package com.marknkamau.justjava.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.onTextChanged(onChange: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            // Do nothing
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChange()
        }
    })
}

val EditText.trimmedText: String
    get() = this.text.trim().toString()

fun TextInputLayout.resetErrorOnChange(editText: EditText) {
    editText.onTextChanged { this.error = null }
}
