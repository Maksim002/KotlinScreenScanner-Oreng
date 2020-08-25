package com.timelysoft.tsjdomcom.service

import com.example.myapplication.model.AuthModel
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
        @POST("login?token=AAFnSAWWHqPe0q")
    suspend fun auth(@FieldMap params: Map<String, String>): Response<AuthModel>
}

