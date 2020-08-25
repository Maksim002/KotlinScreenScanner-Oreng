package com.example.kotlinscreenscanner.service.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorModel (
    @SerializedName("code")
    var code: Int = 0,

    @SerializedName("message")
    var message: String = ""

)