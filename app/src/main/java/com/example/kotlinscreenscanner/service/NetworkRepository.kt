package com.timelysoft.tsjdomcom.service

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers


class NetworkRepository {
    fun auth(params: Map<String, String>) = liveData(Dispatchers.IO) {

        try {
            val response = RetrofitService.apiService().auth(params)
            when {
                response.isSuccessful -> {
                    if (response.body() != null) {
                        emit(ResultStatus.success(response.body()))
                    } else {
                        emit(ResultStatus.error("Неверный логин или пароль"))
                    }
                }
                else -> {
                    emit(ResultStatus.error("Не известная ошибка"))
                }
            }
        } catch (e: Exception) {
            emit(ResultStatus.netwrok("Проблеммы с подключением интернета", null))
        }
    }

    fun numberPhone(map: Map<String,String>) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitService.apiService().numberPhone(map)
            when {
                response.isSuccessful -> {
                    if (response.body() != null) {
                        emit(ResultStatus.success(response.body()))
                    } else {
                        emit(ResultStatus.error("Ваш номер принят"))
                    }
                }
                else -> {
                    emit(ResultStatus.error("Не известная ошибка"))
                }
            }
        } catch (e: Exception) {
            emit(ResultStatus.netwrok("Проблеммы с подключением интернета", null))
        }
    }
}