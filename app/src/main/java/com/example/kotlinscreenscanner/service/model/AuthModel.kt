package com.example.myapplication.model

import com.example.kotlinscreenscanner.service.model.ErrorModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthModel (
    @SerializedName("code")
    @Expose
    var code: Int?,

    @SerializedName("result")
    @Expose
    var result: ResultModel?,

    @SerializedName("error")
    @Expose
    var error: ErrorModel?
)