package com.timelysoft.tsjdomcom.service

import com.example.kotlinscreenscanner.service.model.CommonResponse
import com.example.kotlinscreenscanner.service.model.ResultPhoneModel
import com.example.kotlinscreenscanner.service.model.SmsResultModel
import com.example.myapplication.model.ResultModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("login?token=AAFnSAWWHqPe0q")
    suspend fun auth(@FieldMap params: Map<String, String>): Response<CommonResponse<ResultModel>>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("checkPhone?token=AAFnSAWWHqPe0q")
    suspend fun numberPhone(@FieldMap params: Map<String, String>): Response<CommonResponse<ResultPhoneModel>>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("checkCode?token=AAFnSAWWHqPe0q")
    suspend fun smsConfirmation(@FieldMap params: Map<String, Int>): Response<CommonResponse<SmsResultModel>>
}

