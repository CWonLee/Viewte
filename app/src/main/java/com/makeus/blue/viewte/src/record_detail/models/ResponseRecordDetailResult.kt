package com.makeus.blue.viewte.src.record_detail.models

import com.google.gson.annotations.SerializedName

data class ResponseRecordDetailResult(
    @SerializedName("topic")
    private var topic: String,

    @SerializedName("text")
    private var text: String
) {
    fun getTopic(): String {
        return topic
    }

    fun getText() : String {
        return text
    }
}