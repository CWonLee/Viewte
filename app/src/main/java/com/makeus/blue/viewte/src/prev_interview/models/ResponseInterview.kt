package com.makeus.blue.viewte.src.prev_interview.models

import com.google.gson.annotations.SerializedName
import com.makeus.blue.viewte.src.category.models.ResponseInterviewResult

data class ResponseInterview(
    @SerializedName("isSuccess")
    private var isSuccess: Boolean,

    @SerializedName("code")
    private var code: Int,

    @SerializedName("message")
    private var message: String,

    @SerializedName("result")
    private var result: com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResult
) {
    fun getIsSuccess(): Boolean {
        return isSuccess
    }

    fun getCode(): Int {
        return code
    }

    fun getMessage(): String {
        return message
    }

    fun getResult(): com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResult {
        return result
    }
}