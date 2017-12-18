package com.marknkamau.justjava.utils

import android.widget.EditText

val EditText.trimmedText: String
    get() =  this.text.trim().toString()
