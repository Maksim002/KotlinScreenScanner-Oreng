package com.example.kotlinscreenscanner.ui.login

import android.os.Bundle
import android.view.MenuItem
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.ViewPagerAdapter
import com.example.kotlinscreenscanner.ui.login.fragment.ConfirmationFragment
import com.example.kotlinscreenscanner.ui.login.fragment.NumberFragment
import com.example.kotlinscreenscanner.ui.login.fragment.QuestionnaireFragment
import com.example.kotlinscreenscanner.ui.login.fragment.SmsConfirmationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_registation.*
class RegistrationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(this)
        initViewPager()
    }

    private fun initViewPager() {
        val fragmentAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(NumberFragment())
        fragmentAdapter.addFragment(SmsConfirmationFragment())
        fragmentAdapter.addFragment(QuestionnaireFragment())
        fragmentAdapter.addFragment(ConfirmationFragment())
        registration_viewpager.adapter = fragmentAdapter
        registration_viewpager.addOnPageChangeListener(this)
        registration_viewpager.setOnTouchListener(OnTouchListener { arg0, arg1 -> true })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_news ->{
                registration_viewpager.currentItem = 0
            }
            R.id.navigation_message ->{
                registration_viewpager.currentItem = 1
            }
            R.id.navigation_at_home ->{
                registration_viewpager.currentItem = 2
            }
            R.id.navigation_contacts ->{
                registration_viewpager.currentItem = 3
            }
        }
        return true
    }

    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        when (position){
            0 ->{
                nav_view.selectedItemId = R.id.navigation_news
            }
            1 ->{
                nav_view.selectedItemId = R.id.navigation_message
            }
            2 ->{
                nav_view.selectedItemId = R.id.navigation_at_home
            }
            3 ->{
                nav_view.selectedItemId = R.id.navigation_contacts
            }
        }
        return
    }
}