package com.makeus.blue.viewte.src.join.models

import com.google.gson.annotations.SerializedName

data class ResponseJoin (
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("jwt")
    private var jwt: String
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

    fun getJwt(): String {
        return jwt
    }
}