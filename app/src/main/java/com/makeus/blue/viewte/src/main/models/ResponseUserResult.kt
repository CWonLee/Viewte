package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName

data class ResponseUserResult(
    @SerializedName("userNo")
    private var userNo: Int,

    @SerializedName("name")
    private var name: String,

    @SerializedName("profileUrl")
    private var profileUrl: String
) {
    fun getUserNo(): Int {
        return userNo
    }

    fun getName(): String {
        return name
    }

    fun getProfileUrl() : String {
        return profileUrl
    }
}