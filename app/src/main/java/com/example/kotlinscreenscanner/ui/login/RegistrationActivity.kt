package com.example.kotlinscreenscanner.ui.login

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
//        initViewPager()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle("Регистрация")
    }
//
//    private fun initViewPager() {
//        supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, NumberFragment(this)).commit()
//    }

    override fun onClockListener(position: Int, id: Int) {
        if (position == 0){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, NumberFragment(this)).commit()
        }
        if (position == 1){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, SmsConfirmationFragment(this, id)).commit()
        }
        if (position == 2){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, QuestionnaireFragment(this)).commit()
        }
        if (position == 3){
            supportFragmentManager.beginTransaction().replace(R.id.registration_viewpager, ConfirmationFragment(this)).commit()
        }
    }
}