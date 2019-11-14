package com.marknkamau.justjava.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import com.marknkamau.justjava.R
import com.marknkamau.justjava.ui.BaseActivity
import kotlinx.android.synthetic.main.content_toolbar.*

class CartActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.cart)

    }
}
