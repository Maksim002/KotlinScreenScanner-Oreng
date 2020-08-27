package com.example.kotlinscreenscanner.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.ListenerViewPager
import com.example.myapplication.LoginViewModel
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.fragment_sms_confirmation.*

class SmsConfirmationFragment(var listener: ListenerViewPager, val codeId: Int = -1) : Fragment(){
    private var viewModel = LoginViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sms_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniClick()
    }

    private fun iniClick() {
        sms_confirmation_enter.setOnClickListener {
            val map = HashMap<String, Int>()
            map.put("id", codeId)
            map.put("code", sms_confirmation_check.text.toString().toInt())
            viewModel.smsConfirmation(map).observe(viewLifecycleOwner, Observer { result->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null){
                            Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
                        }else{
                            listener.onClockListener(2)
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