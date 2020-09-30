package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName

class ResponseGetCategory (
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var result: ArrayList<ResponseGetCategoryResult>
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

    fun getResult(): ArrayList<ResponseGetCategoryResult> {
        return result
    }
}