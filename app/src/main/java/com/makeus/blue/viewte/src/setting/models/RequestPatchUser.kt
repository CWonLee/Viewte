package com.makeus.blue.viewte.src.setting.models

import com.google.gson.annotations.SerializedName

class RequestPatchUser(
    @SerializedName("profileUrl")
    private var profileUrl: String,

    @SerializedName("name")
    private var name: String
) {
}