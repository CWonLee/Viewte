package com.makeus.blue.viewte.src.interview_detail.models

import com.google.gson.annotations.SerializedName

data class ResponseGetMemo(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var result: ArrayList<ResponseGetMemoResult>
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

    fun getResult() : ArrayList<ResponseGetMemoResult> {
        return this.result
    }
}