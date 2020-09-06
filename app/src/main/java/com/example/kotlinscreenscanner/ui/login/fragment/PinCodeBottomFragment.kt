package com.example.kotlinscreenscanner.ui.login.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.PintCodeBottomListener
import com.example.kotlinscreenscanner.ui.Top
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.tsjdomcom.service.AppPreferences
import kotlinx.android.synthetic.main.fragment_number_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_pin_code_bottom.*

class PinCodeBottomFragment(private val listener: PintCodeBottomListener) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_code_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniClick()
    }

    private fun iniClick() {
        bottom_sheet_closed.setOnClickListener {
            listener.pinCodeClockListener()
            this.dismiss()
        }

        bottom_sheet_without_code.setOnClickListener {
            listener.pinCodeClockListener()
            this.dismiss()
            AppPreferences.savePin = null
        }

        bottom_sheet_resume.setOnClickListener {
            if (validate()) {
                val numberOne = bottom_sheet_pin_code.text.toString()
                val secondNumber = bottom_sheet_repeat_code.text.toString()
                if (AppPreferences.savePin!!.isNotEmpty()) {
                    if (numberOne == AppPreferences.savePin && secondNumber == AppPreferences.savePin) {
                        val intent = Intent(context, Top::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "Что то не так", Toast.LENGTH_LONG).show()
                    }
                } else {
                    initRequest(numberOne, secondNumber)
                }
            }
        }
    }

    private fun initRequest(numberOne: String, secondNumber: String) {
        if (numberOne.isNotEmpty() && secondNumber.isNotEmpty()){
            AppPreferences.savePin = numberOne
            val intent = Intent(context, Top::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(context, "Смс код не совпадает", Toast.LENGTH_LONG).show()
        }
    }
    // Длина должна быть ровна 4м
    //Длина должна быт оденаковая
    //Пороль должен совподать
    //Пороль не должен быть пустым

    private fun validate(): Boolean {
        var valid = true
        if (bottom_sheet_pin_code.text!!.toString().length != 4) {
            bottom_sheet_pin_code.error = "Поле должно содержать 4 символа"
            valid = false
        } else {
            bottom_sheet_pin_code.error = null
        }

        if (bottom_sheet_repeat_code.text!!.toString().length != 4) {
            bottom_sheet_repeat_code.error = "Поле должно содержать 4 символа"
            valid = false
        } else {
            bottom_sheet_repeat_code.error = null
        }
        return valid
    }
}