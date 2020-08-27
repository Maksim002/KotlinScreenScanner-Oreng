package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinscreenscanner.service.model.CommonResponse
import com.example.kotlinscreenscanner.service.model.NumberPhoneModel
import com.example.kotlinscreenscanner.service.model.ResultPhoneModel
import com.example.kotlinscreenscanner.service.model.SmsResultModel
import com.example.myapplication.model.AuthModel
import com.example.myapplication.model.ResultModel
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.ResultStatus

class LoginViewModel : ViewModel() {

    private val repository = NetworkRepository()

    fun auth(params: Map<String, String>): LiveData<ResultStatus<CommonResponse<ResultModel>>> {
        return repository.auth(params)
    }

    fun numberPhones(phone:  Map<String, String>): LiveData<ResultStatus<CommonResponse<ResultPhoneModel>>> {
        return repository.numberPhone(phone)
    }

    fun smsConfirmation(phone:  Map<String, Int>): LiveData<ResultStatus<CommonResponse<SmsResultModel>>> {
        return repository.smsConfirmation(phone)
    }
}