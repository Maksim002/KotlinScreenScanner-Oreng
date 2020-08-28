package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinscreenscanner.service.model.*
import com.example.myapplication.model.AuthModel
import com.example.myapplication.model.ResultModel
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.ResultStatus
import java.util.*
import kotlin.collections.ArrayList

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

    fun questionnaire(map: Map<String, String>): LiveData<ResultStatus<CommonResponse<QuestionnaireResultModel>>> {
        return repository.questionnaire(map)
    }

    fun listGender(phone:  Map<String, Int>): LiveData<ResultStatus<CommonResponse<ArrayList<ListGenderResultModel>>>> {
        return repository.listGender(phone)
    }

    fun listNationality(phone:  Map<String, Int>): LiveData<ResultStatus<CommonResponse<ArrayList<ListNationalityResultModel>>>> {
        return repository.listNationality(phone)
    }

    fun listSecretQuestion(phone:  Map<String, Int>): LiveData<ResultStatus<CommonResponse<ArrayList<ListSecretQuestionResultModel>>>> {
        return repository.listSecretQuestion(phone)
    }
}