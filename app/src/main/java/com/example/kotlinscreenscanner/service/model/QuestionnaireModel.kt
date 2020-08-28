package com.example.kotlinscreenscanner.service.model

import com.google.gson.annotations.SerializedName

data class QuestionnaireModel (
    @SerializedName("last_name")
    var last_name: String = "",

    @SerializedName("first_name")
    var first_name: String = "",

    @SerializedName("second_name")
    var second_name: String = "",

    @SerializedName("u_date")
    var u_date: String = "",

    @SerializedName("gender")
    var gender: Int = 0,

    @SerializedName("nationality")
    var nationality: Int = 0,

    @SerializedName("first_phone")
    var first_phone: String = "",

    @SerializedName("second_phone")
    var second_phone: String = "",

    @SerializedName("question")
    var question: Int = 0,

    @SerializedName("response")
    var response: String = "",

    @SerializedName("sms_code")
    var sms_code: Int = 0
)