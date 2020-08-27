package com.example.kotlinscreenscanner.ui.login

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.ListenerViewPager
import com.example.kotlinscreenscanner.ui.login.fragment.ConfirmationFragment
import com.example.kotlinscreenscanner.ui.login.fragment.NumberFragment
import com.example.kotlinscreenscanner.ui.login.fragment.QuestionnaireFragment
import com.example.kotlinscreenscanner.ui.login.fragment.SmsConfirmationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_registation.*

class RegistrationActivity : AppCompatActivity(), ListenerViewPager{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registation)
        initViewPager()
    }

    private fun initViewPager() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            var selectedFragment = androidx.fragment.app.Fragment()
            when (menuItem.itemId) {
                R.id.navigation_news ->
                    selectedFragment = NumberFragment(this)
                R.id.navigation_message ->
                    selectedFragment = SmsConfirmationFragment(this)
                R.id.navigation_at_home ->
                    selectedFragment = QuestionnaireFragment(this)
                R.id.navigation_contacts -> {
                    selectedFragment = ConfirmationFragment(this)
                }
            }
            assert(selectedFragment != null)
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, selectedFragment).commit()
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, NumberFragment(this)).commit()
    }

    override fun onClockListener(position: Int, id: Int) {
        if (position == 1){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, SmsConfirmationFragment(this, id)).commit()
            nav_view.menu.getItem(1).setChecked(true)
        }
        if (position == 2){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, QuestionnaireFragment(this)).commit()
            nav_view.menu.getItem(2).setChecked(true)
        }
        if (position == 3){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, ConfirmationFragment(this)).commit()
            nav_view.menu.getItem(3).setChecked(true)
        }
    }
}