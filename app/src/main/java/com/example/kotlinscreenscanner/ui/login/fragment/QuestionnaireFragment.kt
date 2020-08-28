package com.example.kotlinscreenscanner.ui.login.fragment

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlinscreenscanner.R
import com.example.kotlinscreenscanner.adapter.ListenerViewPager
import com.example.kotlinscreenscanner.service.model.*
import com.example.myapplication.LoginViewModel
import com.timelysoft.tsjdomcom.service.AppPreferences.toFullPhone
import com.timelysoft.tsjdomcom.service.Status
import com.timelysoft.tsjdomcom.utils.MyUtils
import kotlinx.android.synthetic.main.fragment_questionnaire.*
import java.util.*
import kotlin.collections.ArrayList

class QuestionnaireFragment(var listener: ListenerViewPager) : Fragment() {
    private var viewModel = LoginViewModel()
    private var mLastClickTime: Long = 0
    private var data: String = ""
    private var idSex: Int = 0
    private var listNationalityId: Int = 0
    private var listSecretQuestionId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAutoDatesFrom()
        getIdSxs()
        getListNationality()
        getListSecretQuestion()
        initClock()
    }

    private fun initClock() {
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
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer { result ->
                    val msg = result.msg
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
                            if (data!!.result == null) {
                                Toast.makeText(context, data.error.message, Toast.LENGTH_LONG).show()
                            } else {
                                listener.onClockListener(3)
                            }
                        }
                        Status.ERROR, Status.NETWORK -> {
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    private fun getAutoDatesFrom() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MARCH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        questionnaire_date_birth.keyListener = null;
        questionnaire_date_birth.setOnFocusChangeListener setOnClickListener@{ _, hasFocus ->
            if (hasFocus) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                questionnaire_date_birth_out.defaultHintTextColor =
                    ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
                val picker = activity?.let {
                    DatePickerDialog(
                        it, R.style.DatePicker, { _, year1, monthOfYear, dayOfMonth ->
                            questionnaire_date_birth.setText(
                                MyUtils.convertDate(
                                    dayOfMonth,
                                    monthOfYear + 1,
                                    year1
                                )
                            )
                            data = (MyUtils.convertDateServer(year1, monthOfYear + 1, dayOfMonth))
                        }, year, month, day
                    )
                }
                picker!!.show()
                questionnaire_date_birth.clearFocus()
            }
        }
    }

    private fun getIdSxs() {
        var list:  ArrayList<ListGenderResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listGender(map).observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterIdSxs = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_sxs.setAdapter(adapterIdSxs)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        })

        questionnaire_id_sxs.keyListener = null
        ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
        questionnaire_id_sxs.onItemClickListener =
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
                    if (!hasFocus && questionnaire_id_sxs.text!!.isNotEmpty()) {
                        questionnaire_id_sxs_out.defaultHintTextColor =
                            ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
                        questionnaire_id_sxs_out.isErrorEnabled = false
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
        viewModel.listNationality(map).observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListNationality = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_nationality.setAdapter(adapterListNationality)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
        questionnaire_id_nationality.keyListener = null
        ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
        questionnaire_id_nationality.onItemClickListener =
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
                    if (!hasFocus && questionnaire_id_nationality.text!!.isNotEmpty()) {
                        questionnaire_id_nationality_out.defaultHintTextColor =
                            ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
                        questionnaire_id_nationality_out.isErrorEnabled = false
                    }
                } catch (e: Exception) {
                }
            }
        questionnaire_id_nationality.clearFocus()
    }

    private fun getListSecretQuestion() {
        var list:  ArrayList<ListSecretQuestionResultModel> = arrayListOf()
        val map = HashMap<String, Int>()
        map.put("id", 0)
        viewModel.listSecretQuestion(map).observe(viewLifecycleOwner, androidx.lifecycle.Observer { result->
            val msg = result.msg
            val data = result.data
            when (result.status) {
                Status.SUCCESS -> {
                    val adapterListSecretQuestion = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, data!!.result)
                    list = data.result
                    questionnaire_id_secret.setAdapter(adapterListSecretQuestion)
                }
                Status.ERROR, Status.NETWORK -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        })

        questionnaire_id_secret.keyListener = null
        ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
        questionnaire_id_secret.onItemClickListener =
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
                    if (!hasFocus && questionnaire_id_secret.text!!.isNotEmpty()) {
                        questionnaire_id_secret_out.defaultHintTextColor =
                            ColorStateList.valueOf(resources.getColor(R.color.orangeColor))
                        questionnaire_id_secret_out.isErrorEnabled = false
                    }
                } catch (e: Exception) {
                }
            }
        questionnaire_id_secret.clearFocus()
    }
}