package com.makeus.blue.viewte.src.login.models

import com.google.gson.annotations.SerializedName

data class RequestLogin(
    @SerializedName("access_token")
    var access_token: String
)