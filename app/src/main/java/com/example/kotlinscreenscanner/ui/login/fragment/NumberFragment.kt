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
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.AppPreferences.toFullPhone
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.fragment_number.*

class NumberFragment(var listenr: ListenerViewPager) : Fragment() {
    private var viewModel = NetworkRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick() {
        number_next.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("phone", number_phone.text.toString().toFullPhone())
            viewModel.numberPhone(map).observe(viewLifecycleOwner, Observer { result->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null){
                            Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
                        }else{
                            listenr.onClockListener( data.result.id!!)
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