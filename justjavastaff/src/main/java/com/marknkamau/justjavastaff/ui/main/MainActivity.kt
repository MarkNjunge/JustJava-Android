package com.marknkamau.justjavastaff.ui.main

import android.content.Intent
import com.google.android.material.tabs.TabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import com.marknkamau.justjavastaff.JustJavaStaffApp

import com.marknkamau.justjavastaff.R
import com.marknkamau.justjavastaff.ui.BaseActivity
import com.marknkamau.justjavastaff.ui.login.LogInActivity
import com.marknkamau.justjavastaff.ui.orders.OrdersFragment
import com.marknkamau.justjavastaff.ui.payments.PaymentsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val auth = (application as JustJavaStaffApp).auth
        if (!auth.isSignedIn()) {
            startActivity(Intent(this@MainActivity, LogInActivity::class.java))
            finish()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = sectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    inner class SectionsPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            return when (position) {
                0 -> OrdersFragment()
                1 -> PaymentsFragment()
                else -> OrdersFragment()
            }
        }

        override fun getCount() = 2
    }

}
