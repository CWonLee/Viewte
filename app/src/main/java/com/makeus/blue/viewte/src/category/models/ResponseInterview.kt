package com.makeus.blue.viewte.src.category.models

import com.google.gson.annotations.SerializedName

data class ResponseInterview(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var result: ArrayList<ResponseInterviewResult>
) {
    fun IsSuccess(): Boolean {
        return this.isSuccess
    }

    fun getCode(): Int {
        return this.code
    }

    fun getMessage(): String {
        return this.message
    }

    fun getResult(): ArrayList<ResponseInterviewResult> {
        return this.result
    }
}