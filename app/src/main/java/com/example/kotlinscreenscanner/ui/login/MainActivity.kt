package com.example.kotlinscreenscanner.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.ui.Top
import com.example.myapplication.LoginViewModel
import com.timelysoft.tsjdomcom.service.Status
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iniClick()
    }

    private fun iniClick() {
        main_show.setOnClickListener {
            main_text_password.transformationMethod = null
        }

        main_registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        main_enter.setOnClickListener {
            val map = HashMap<String, String>()
            map.put("password", main_text_password.text.toString())
            map.put("login", main_text_login.text.toString())
            viewModel.auth(map).observe(this, Observer { result ->
                val msg = result.msg
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.result == null){
                            Toast.makeText(this, data.generalError.message, Toast.LENGTH_LONG).show()
                        }else{
                            startMainActivity()
                        }
                    }
                    Status.ERROR, Status.NETWORK -> {
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }
    private fun startMainActivity() {
        val intent = Intent(this, Top::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        main_text_password.setTransformationMethod(PasswordTransformationMethod())

    }
}