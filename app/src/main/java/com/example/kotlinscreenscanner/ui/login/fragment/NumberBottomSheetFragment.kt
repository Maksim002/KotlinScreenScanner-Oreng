package com.example.kotlinscreenscanner.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.ui.login.QuestionnaireActivity
import com.example.myapplication.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.fragment_number_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_sms_confirmation.*

class NumberBottomSheetFragment(var idPhone: Int) : BottomSheetDialogFragment() {
    private var viewModel = LoginViewModel()

    companion object {
        lateinit var sheet: NumberBottomSheetFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_number_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick() {
        number_bottom_code.setOnClickListener {
            this.dismiss()
        }
        closed.setOnClickListener {
            this.dismiss()
        }

        number_next.setOnClickListener {
            val map = HashMap<String, Int>()
            map.put("id", idPhone)
            map.put("code", number_text_sms.text.toString().toInt())
            viewModel.smsConfirmation(map).observe(viewLifecycleOwner, Observer { result->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null){
                            Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
                        }else{
                            val intent = Intent(context, QuestionnaireActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    Status.ERROR, Status.NETWORK -> {
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }
}