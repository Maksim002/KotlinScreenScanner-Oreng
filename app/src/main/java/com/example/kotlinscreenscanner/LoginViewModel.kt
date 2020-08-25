package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.AuthModel
import com.example.myapplication.model.ResultModel
import com.timelysoft.tsjdomcom.service.NetworkRepository
import com.timelysoft.tsjdomcom.service.ResultStatus
import java.util.*

class LoginViewModel : ViewModel() {

    private val repository = NetworkRepository()

    fun auth(params: Map<String, String>): LiveData<ResultStatus<AuthModel>> {
        return repository.auth(params)
    }
}