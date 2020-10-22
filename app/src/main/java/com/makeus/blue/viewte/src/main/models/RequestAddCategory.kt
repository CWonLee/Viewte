package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName

data class RequestAddCategory (
    @SerializedName("c_title")
    private var c_title: String,

    @SerializedName("imageUrl")
    private var imageUrl: String
) {
}