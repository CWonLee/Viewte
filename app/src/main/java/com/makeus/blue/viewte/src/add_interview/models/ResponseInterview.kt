package com.makeus.blue.viewte.src.add_interview.models

import com.google.gson.annotations.SerializedName

data class ResponseInterview(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String
) {
    fun isSuccess(): Boolean {
        return this.isSuccess
    }

    fun getCode(): Int {
        return this.code
    }

    fun getMessage(): String {
        return this.message
    }
}