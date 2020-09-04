package com.example.kotlinscreenscanner.ui.login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.service.model.CounterResultModel
import com.example.kotlinscreenscanner.ui.login.fragment.NumberBottomSheetFragment
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.Status
import com.timelysoft.tsjdomcom.utils.MyUtils
import kotlinx.android.synthetic.main.activity_number.*
import java.util.*
import kotlin.collections.HashMap

class NumberActivity : AppCompatActivity() {
    private var viewModel = NetworkRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)
        number_phone.requestFocus()
        initToolBar()
        initClick()
        getListCountry()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initClick() {
        MainActivity.alert.show()
        number_next.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("phone", MyUtils.toFormatMask(number_phone.text.toString()))
            viewModel.numberPhone(map).observe(this, Observer { result ->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null) {
                            Toast.makeText(this, data.error.message, Toast.LENGTH_LONG).show()
                        } else {
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

    private fun getListCountry() {
        var list: ArrayList<CounterResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        MainActivity.alert.show()
        viewModel.listAvailableCountry(map).observe(this, androidx.lifecycle.Observer { result ->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListCountry = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        data!!.result
                    )
                    number_list_country.setAdapter(adapterListCountry)
                    list = data.result
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
            MainActivity.alert.hide()
        })
        number_list_country.keyListener = null
        number_list_country.setOnItemClickListener { adapterView, view, position, l ->
            number_list_country.showDropDown()
            AppPreferences.isFormatMask = list[position].phoneMask
            number_phone.setText("")
            number_phone.mask = ""
            if (position == 0) {
                number_phone.mask = list[position].phoneMask
            } else if (position == 1) {
                number_phone.mask = list[position].phoneMask
            } else if (position == 2) {
                number_phone.mask = list[position].phoneMask
            }
            if (number_list_country.text.toString() != ""){
                number_phone_visibility.visibility = View.VISIBLE
            }

            number_list_country.clearFocus()
        }
        number_list_country.setOnClickListener {
            number_list_country.showDropDown()
        }
        number_list_country.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            try {
                if (hasFocus) {
                    number_list_country.showDropDown()
                }
            } catch (e: Exception) {
            }
        }
    }
}