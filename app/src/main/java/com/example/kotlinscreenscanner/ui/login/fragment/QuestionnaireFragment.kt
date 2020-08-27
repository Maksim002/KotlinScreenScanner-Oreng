package com.example.kotlinscreenscanner.ui.login.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.ListenerViewPager
import kotlinx.android.synthetic.main.fragment_questionnaire.*

class QuestionnaireFragment(var listener: ListenerViewPager) : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_enter.setOnClickListener {
            listener.onClockListener(3)
        }
    }
}