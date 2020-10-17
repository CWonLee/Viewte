package com.makeus.blue.viewte.src.prev_interview.models

import com.google.gson.annotations.SerializedName

data class ResponseInterviewResultInterview(
    @SerializedName("i_title")
    private var i_title: String,

    @SerializedName("purpose")
    private var purpose: String,

    @SerializedName("date")
    private var date: String,

    @SerializedName("time")
    private var time: String,

    @SerializedName("location")
    private var location: String
) {
    fun getITitle(): String {
        return i_title
    }

    fun getPurpose(): String {
        return purpose
    }

    fun getDate(): String {
        return date
    }

    fun getTime(): String {
        return time
    }

    fun getLocation(): String {
        return location
    }
}