package com.example.kotlinscreenscanner.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.service.model.CounterResultModel
import com.example.kotlinscreenscanner.ui.login.fragment.NumberBottomSheetFragment
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.AppPreferences.toFullPhone
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.activity_number.*
import kotlinx.android.synthetic.main.activity_number.number_next
import kotlinx.android.synthetic.main.activity_number.number_phone
import java.util.ArrayList

class NumberActivity : AppCompatActivity() {
    private var viewModel = NetworkRepository()
    private var countryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)
        initClick()
        initToolBar()
        getListCountry()
        initMaskPhone()
    }

    private fun initMaskPhone() {

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

    private fun getListCountry() {
        var list: ArrayList<CounterResultModel> = arrayListOf()
        val map = java.util.HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listAvailableCountry(map).observe(this, androidx.lifecycle.Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListCountry = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    number_list_country.setAdapter(adapterListCountry)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
        number_list_country.keyListener = null
        AdapterView.OnItemClickListener { parent, _, position, _ ->
            number_list_country.showDropDown()
            parent.getItemAtPosition(position).toString()
            countryId = list[position].id!!
            number_list_country.clearFocus()
        }
        number_list_country.setOnClickListener {
            number_list_country.showDropDown()
        }
        number_list_country.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                try {
                    if (hasFocus) {
                        number_list_country.showDropDown()
                    }
                } catch (e: Exception) {
                }
            }
        number_list_country.clearFocus()
    }
}