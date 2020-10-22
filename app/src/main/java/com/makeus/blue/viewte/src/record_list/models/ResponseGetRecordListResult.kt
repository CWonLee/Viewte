package com.makeus.blue.viewte.src.record_list.models

import com.google.gson.annotations.SerializedName

data class ResponseGetRecordListResult(
    @SerializedName("noiseNo")
    private var noiseNo: Int,

    @SerializedName("topic")
    private var topic: String
) {
    fun getNoiseNo(): Int {
        return noiseNo
    }

    fun getTopic() : String {
        return topic
    }
}