package com.marknkamau.justjavastaff.ui.main

import android.content.Intent
import android.support.design.widget.TabLayout

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> OrdersFragment()
                1 -> PaymentsFragment()
                else -> OrdersFragment()
            }
        }

        override fun getCount() = 2
    }

}
