package com.makeus.blue.viewte.src.category.models

import com.google.gson.annotations.SerializedName

class ResponsePatchCategory(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String
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
}