package com.makeus.blue.viewte.src.join.models

import com.google.gson.annotations.SerializedName

data class RequestJoin(
    @SerializedName("name")
    private var name: String,

    @SerializedName("oauthid")
    private var oauthid: String,

    @SerializedName("c_title")
    private var c_title: String
)