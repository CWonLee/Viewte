package com.makeus.blue.viewte.src.record.models

import com.google.gson.annotations.SerializedName

class RequestRecord(
    @SerializedName("topic")
    private var topic: String,

    @SerializedName("text")
    private var text: String
) {
}