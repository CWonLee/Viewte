package com.makeus.blue.viewte.src.trash.models

import com.google.gson.annotations.SerializedName

data class ResponseGetTrashResult(
    @SerializedName("interviewNo")
    private var interviewNo: Int,

    @SerializedName("i_title")
    private var i_title: String,

    @SerializedName("ismemo")
    private var ismemo: Char,

    @SerializedName("date")
    private var date: String,

    @SerializedName("time")
    private var time: String,

    @SerializedName("week")
    private var week: Int
) {
    fun getInterviewNo(): Int {
        return interviewNo
    }

    fun getITitle(): String {
        return i_title
    }

    fun getIsMemo(): Char {
        return ismemo
    }

    fun getDate(): String {
        return date
    }

    fun getTime(): String {
        return time
    }

    fun getWeek(): Int {
        return week
    }
}