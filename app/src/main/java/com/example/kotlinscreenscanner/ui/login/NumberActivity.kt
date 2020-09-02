package com.example.kotlinscreenscanner.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.ui.login.fragment.NumberBottomSheetFragment
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.AppPreferences.toFullPhone
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.activity_number.*
import kotlinx.android.synthetic.main.activity_number.number_next
import kotlinx.android.synthetic.main.activity_number.number_phone
import kotlinx.android.synthetic.main.fragment_number_bottom_sheet.*

class NumberActivity : AppCompatActivity() {
    private var viewModel = NetworkRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)
        initClick()
        initToolBar()
    }

    private fun initBottomSheet(id: Int) {
        val bottomSheetDialogFragment = NumberBottomSheetFragment(id)
        bottomSheetDialogFragment.isCancelable = false;
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Регистрация"
    }

    private fun initClick() {
        MainActivity.alert.show()
        number_next.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("phone", number_phone.text.toString().toFullPhone())
            viewModel.numberPhone(map).observe(this, Observer { result->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null){
                            Toast.makeText(this, data.error.message, Toast.LENGTH_LONG).show()
                        }else{
                            AppPreferences.number = number_phone.text.toString()
                            initBottomSheet(data.result.id!!)
                        }
                    }
                    Status.ERROR, Status.NETWORK -> {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                }
            })
            MainActivity.alert.hide()
        }
    }
}