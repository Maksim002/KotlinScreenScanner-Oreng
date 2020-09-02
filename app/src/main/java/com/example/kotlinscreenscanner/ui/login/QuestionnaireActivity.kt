package com.example.kotlinscreenscanner.ui.login

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.content.res.ColorStateList.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.service.model.ListGenderResultModel
import com.example.kotlinscreenscanner.service.model.ListNationalityResultModel
import com.example.kotlinscreenscanner.service.model.ListSecretQuestionResultModel
import com.example.myapplication.LoginViewModel
import com.timelysoft.tsjdomcom.service.AppPreferences
import com.timelysoft.tsjdomcom.service.AppPreferences.toFullPhone
import com.timelysoft.tsjdomcom.service.Status
import com.timelysoft.tsjdomcom.utils.MyUtils
import kotlinx.android.synthetic.main.actyviti_questionnaire.*
import kotlinx.android.synthetic.main.actyviti_questionnaire.main_enter
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_phone_additional
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_phone_number
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_secret_response
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_sms_code
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_text_name
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_text_patronymics
import kotlinx.android.synthetic.main.actyviti_questionnaire.questionnaire_text_surnames
import java.util.*

class QuestionnaireActivity : AppCompatActivity() {
    private var viewModel = LoginViewModel()
    private var data: String = ""
    private var idSex: Int = 0
    private var listNationalityId: Int = 0
    private var listSecretQuestionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actyviti_questionnaire)
        initToolBar()
        iniClock()
        iniData()
        getIdSxs()
        getListNationality()
        getListSecretQuestion()
    }

    private fun iniClock() {
        questionnaire_agreement.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                main_enter.isClickable = true
                main_enter.setBackgroundColor(resources.getColor(R.color.orangeColor))
            }else{
                main_enter.isClickable = false
                main_enter.setBackgroundColor(resources.getColor(R.color.blueColor))
            }
        }

        main_enter.setOnClickListener {
            val map = mutableMapOf<String, String>()
            map["last_name"] = questionnaire_text_surnames.text.toString()
            map["first_name"] = questionnaire_text_name.text.toString()
            map["second_name"] = questionnaire_text_patronymics.text.toString()
            map["u_date"] = MyUtils.toServerDate(data)
            map["gender"] = idSex.toString()
            map["nationality"] = listNationalityId.toString()
            map["first_phone"] = questionnaire_phone_number.text.toString().toFullPhone()
            map["second_phone"] = questionnaire_phone_additional.text.toString().toFullPhone()
            map["second_phone"] = questionnaire_phone_additional.text.toString().toFullPhone()
            map["question"] = listSecretQuestionId.toString()
            map["response"] = questionnaire_secret_response.text.toString()
            map["sms_code"] = questionnaire_sms_code.text.toString()

            viewModel.questionnaire(map)
                .observe(this, Observer { result ->
                    val msg = result.msg
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (data!!.result == null) {
                                Toast.makeText(this, data.error.message, Toast.LENGTH_LONG).show()
                            } else {

                            }
                        }
                        Status.ERROR, Status.NETWORK -> {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    private fun initToolBar() {
        setSupportActionBar(questionnaire_toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Регистрация"
    }

    override fun onStart() {
        super.onStart()
        main_enter.isClickable = false
        main_enter.setBackgroundColor(resources.getColor(R.color.blueColor))
        questionnaire_phone_number.setText(AppPreferences.number.toString())
    }

    private fun iniData() {
        questionnaire_date_birth.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
                val dialog = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    { _, year, month, dayOfMonth ->
                        questionnaire_date_birth.setText(
                            MyUtils.convertDate(
                                dayOfMonth,
                                month + 1,
                                year
                            )
                        )
                        data = (MyUtils.convertDateServer(year, month + 1, dayOfMonth))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
            false
        }
    }

    private fun getIdSxs() {
        var list:  ArrayList<ListGenderResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listGender(map).observe(this, androidx.lifecycle.Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterIdSxs = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_sxs.setAdapter(adapterIdSxs)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
        questionnaire_id_sxs.keyListener = null
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                questionnaire_id_sxs.showDropDown()
                parent.getItemAtPosition(position).toString()
                idSex = list[position].id!!
                questionnaire_id_sxs.clearFocus()
            }
        questionnaire_id_sxs.setOnClickListener {
            questionnaire_id_sxs.showDropDown()
        }
        questionnaire_id_sxs.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                try {
                    if (hasFocus) {
                        questionnaire_id_sxs.showDropDown()
                    }
                } catch (e: Exception) {
                }
            }
        questionnaire_id_sxs.clearFocus()
    }

    private fun getListNationality() {
        var list:  ArrayList<ListNationalityResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listNationality(map).observe(this, Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListNationality = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_nationality.setAdapter(adapterListNationality)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
        questionnaire_id_nationality.keyListener = null
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                questionnaire_id_nationality.showDropDown()
                parent.getItemAtPosition(position).toString()
                listNationalityId = list[position].id!!
                questionnaire_id_nationality.clearFocus()
            }
        questionnaire_id_nationality.setOnClickListener {
            questionnaire_id_nationality.showDropDown()
        }
        questionnaire_id_nationality.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                try {
                    if (hasFocus) {
                        questionnaire_id_nationality.showDropDown()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        questionnaire_id_nationality.clearFocus()
    }

    private fun getListSecretQuestion() {
        var list:  ArrayList<ListSecretQuestionResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listSecretQuestion(map).observe(this, Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListSecretQuestion = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_secret.setAdapter(adapterListSecretQuestion)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
        })

        questionnaire_id_secret.keyListener = null
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                questionnaire_id_secret.showDropDown()
                parent.getItemAtPosition(position).toString()
                listSecretQuestionId = list[position].id!!
                questionnaire_id_secret.clearFocus()
            }
        questionnaire_id_secret.setOnClickListener {
            questionnaire_id_secret.showDropDown()
        }
        questionnaire_id_secret.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                try {
                    if (hasFocus) {
                        questionnaire_id_secret.showDropDown()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        questionnaire_id_secret.clearFocus()
    }
}