package com.makeus.blue.viewte.src.login.models

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("jwt")
    private var jwt: String,

    @SerializedName("oauthid")
    private var oauthid: String,

    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String
) {
    fun getJwt(): String {
        return jwt
    }

    fun IsSuccess(): Boolean {
        return isSuccess
    }

    fun getCode(): Int {
        return code
    }

    fun getMessage(): String {
        return message
    }

    fun getOauthid(): String {
        return oauthid
    }
}