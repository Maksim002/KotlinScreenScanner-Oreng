package com.example.kotlinscreenscanner.ui.login.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.PintCodeBottomListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
            this.dismiss()
        }

        bottom_sheet_without_code.setOnClickListener {
            listener.pinCodeClockListener()
            this.dismiss()
        }
    }
}