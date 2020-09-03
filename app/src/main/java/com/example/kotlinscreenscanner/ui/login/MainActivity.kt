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
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.Status
import com.timelysoft.tsjdomcom.utils.LoadingAlert
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var viewModel = LoginViewModel()
    private var tokenId = ""

    companion object {
        lateinit var alert: LoadingAlert
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppPreferences.init(application)
        initCheck()
        iniClick()
        alert = LoadingAlert(this)
    }

    private fun iniClick() {
        main_show.setOnClickListener {
            if (AppPreferences.isValid){
                AppPreferences.isValid = false
                main_text_password.transformationMethod = PasswordTransformationMethod()
            }else{
                AppPreferences.isValid = true
                main_text_password.transformationMethod = null
            }
        }

        main_registration.setOnClickListener {
            val intent = Intent(this, NumberActivity::class.java)
            startActivity(intent)
        }

        main_enter.setOnClickListener {
            if (validate()) {
                val map = HashMap<String, String>()
                map.put("password", main_text_password.text.toString())
                map.put("login", main_text_login.text.toString())
                viewModel.auth(map).observe(this, Observer { result ->
                    val msg = result.msg
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (data!!.result == null) {
                                Toast.makeText(this, data.error.message, Toast.LENGTH_LONG).show()
                            } else {
                                tokenId = data.result.token
                                startMainActivity()
                                if (main_remember_username.isChecked) {
                                    AppPreferences.isRemember = main_remember_username.isChecked
                                    viewModel.save(main_text_login.text.toString(), data.result.token)
                                }else{
                                    AppPreferences.isRemember = false
                                    AppPreferences.clearLogin()
                                }
                            }
                        }
                        Status.ERROR, Status.NETWORK -> {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
        }
    }

    private fun initCheck() {
        if (AppPreferences.isRemember){
            main_remember_username.isChecked = AppPreferences.isRemember
            main_text_login.setText(AppPreferences.login)
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (main_text_login.text.toString().isEmpty()) {
            main_text_login.error = "Введите логин"
            valid = false
        }

        if (main_text_password.text.toString().isEmpty()) {
            main_text_password.error = "Введите пароль"
            valid = false
        }
        return valid
    }

    private fun startMainActivity() {
        val intent = Intent(this, Top::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        AppPreferences.isValid = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}