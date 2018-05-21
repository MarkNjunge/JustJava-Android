package com.marknkamau.justjava.ui

import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import com.marknkamau.justjava.ui.main.MainActivity


/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
