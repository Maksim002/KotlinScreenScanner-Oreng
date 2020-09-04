package com.example.kotlinscreenscanner.ui.login

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.PintCodeBottomListener
import com.example.kotlinscreenscanner.ui.Top
import com.example.kotlinscreenscanner.ui.login.fragment.PinCodeBottomFragment
import com.example.myapplication.LoginViewModel
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.Status
import com.timelysoft.tsjdomcom.utils.LoadingAlert
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity(), PintCodeBottomListener {
    private var viewModel = LoginViewModel()
    private var tokenId = ""

    companion object {
        lateinit var alert: LoadingAlert
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppPreferences.init(application)
        iniClick()
        initCheck()
        alert = LoadingAlert(this)
    }

    private fun iniClick() {
        main_show.setOnClickListener {
            if (AppPreferences.isValid) {
                AppPreferences.isValid = false
                main_text_password.transformationMethod = PasswordTransformationMethod()
            } else {
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
                alert.show()
                viewModel.auth(map).observe(this, Observer { result ->
                    val msg = result.msg
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (data!!.result == null) {
                                Toast.makeText(this, data.error.message, Toast.LENGTH_LONG).show()
                            } else {
                                tokenId = data.result.token
                                if (main_login_code.isChecked){
                                    initBottomSheet()
                                }else{
                                    startMainActivity()
                                }
                                if (main_remember_username.isChecked) {
                                    AppPreferences.isRemember = main_remember_username.isChecked
                                    AppPreferences.isTouchId = main_touch_id.isChecked
                                    AppPreferences.isLoginCode = main_login_code.isChecked
                                    viewModel.save(
                                        main_text_login.text.toString(),
                                        data.result.token
                                    )
                                    AppPreferences.password = main_text_password.text.toString()
                                } else {
                                    AppPreferences.isRemember = false
                                    AppPreferences.clearLogin()
                                }
                            }
                        }
                        Status.ERROR, Status.NETWORK -> {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                    alert.hide()
                })
            }
        }
    }

    private fun initCheck() {
        if (AppPreferences.isRemember) {
            main_remember_username.isChecked = AppPreferences.isRemember
            main_text_login.setText(AppPreferences.login)
        }

        if (AppPreferences.isTouchId) {
            main_touch_id.isChecked = AppPreferences.isTouchId
            iniTouchId()
        }

        if (AppPreferences.isLoginCode) {
            main_login_code.isChecked = AppPreferences.isLoginCode
            initBottomSheet()
        }

        main_login_code.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                main_touch_id.isChecked = false
                main_remember_username.isChecked = true
                main_remember_username.isClickable = false
            } else {
                main_remember_username.isClickable = true
                AppPreferences.isLoginCode = false
            }
        }

        main_touch_id.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                main_login_code.isChecked = false
                main_remember_username.isChecked = true
                main_remember_username.isClickable = false
            } else {
                main_remember_username.isClickable = true
                AppPreferences.isTouchId = false
            }
        }
    }

    private fun initBottomSheet() {
        val bottomSheetDialogFragment = PinCodeBottomFragment(this)
        bottomSheetDialogFragment.isCancelable = false;
        bottomSheetDialogFragment.show(supportFragmentManager, bottomSheetDialogFragment.tag)
    }

    override fun pinCodeClockListener() {
        main_login_code.isChecked = false
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

    private fun iniTouchId() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                authUser(executor)
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->{
                Toast.makeText(this, getString(R.string.error_msg_no_biometric_hardware), Toast.LENGTH_LONG).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this, getString(R.string.error_msg_biometric_hw_unavailable), Toast.LENGTH_LONG).show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, getString(R.string.error_msg_biometric_not_setup), Toast.LENGTH_LONG).show()
                AppPreferences.isTouchId = false
                main_touch_id.isChecked = false
            }
        }
    }

    private fun authUser(executor: Executor) {
        // 1
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            // 2
            .setTitle(getString(R.string.auth_title))
            // 3
            .setSubtitle(getString(R.string.auth_subtitle))
            // 4
            .setDescription(getString(R.string.auth_description))
            // 5
            .setDeviceCredentialAllowed(false)
            // 6
            .setNegativeButtonText("Отмена")
            // 7
            .build()

        // 1
        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                // 2
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val map = HashMap<String, String>()
                    map.put("password", AppPreferences.password.toString())
                    map.put("login", main_text_login.text.toString())
                    alert.show()
                    viewModel.auth(map).observe(this@MainActivity, Observer { result ->
                        val msg = result.msg
                        val data = result.data
                        when (result.status) {
                            Status.SUCCESS -> {
                                if (data!!.result == null) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        data.error.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    tokenId = data.result.token
                                    viewModel.save(
                                        main_text_login.text.toString(),
                                        data.result.token
                                    )
                                    val intent = Intent(this@MainActivity, Top::class.java)
                                    startActivity(intent)
                                }
                            }
                            Status.ERROR, Status.NETWORK -> {
                                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                            }
                        }
                        alert.hide()
                    })
                }

                // 3
                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_msg_auth_error, errString),
                        Toast.LENGTH_SHORT
                    ).show()
                    main_touch_id.isChecked = false
                }

                // 4
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_msg_auth_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}