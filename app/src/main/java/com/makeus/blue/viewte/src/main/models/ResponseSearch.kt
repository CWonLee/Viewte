package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName

class ResponseSearch(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var result: ArrayList<ResponseSearchResult>
) {
    fun IsSuccess(): Boolean {
        return isSuccess
    }

    fun getCode(): Int {
        return code
    }

    fun getMessage(): String {
        return message
    }

    fun getResult(): ArrayList<ResponseSearchResult> {
        return result
    }
}