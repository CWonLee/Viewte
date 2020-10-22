package com.makeus.blue.viewte.src.record_list.models

import com.google.gson.annotations.SerializedName

data class ResponseGetRecordList(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var listResult: ArrayList<ResponseGetRecordListResult>
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

    fun getResult(): ArrayList<ResponseGetRecordListResult> {
        return listResult
    }
}