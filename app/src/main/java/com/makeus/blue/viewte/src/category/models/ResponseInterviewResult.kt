package com.makeus.blue.viewte.src.category.models

import com.google.gson.annotations.SerializedName

data class ResponseInterviewResult(
    @SerializedName("interviewNo")
    private var interviewNo: Int,

    @SerializedName("i_title")
    private var i_title: String,

    @SerializedName("ismemo")
    private var ismemo: Char,

    @SerializedName("date")
    private var date: String,

    @SerializedName("week")
    private var week: Int,

    @SerializedName("imageUrl")
    private var imageUrl: String
) {
    fun getInterviewNo(): Int {
        return this.interviewNo
    }

    fun getITitle(): String {
        return this.i_title
    }

    fun getIsMemo(): Char {
        return this.ismemo
    }

    fun getDate(): String {
        return this.date
    }

    fun getWeek(): Int {
        return this.week
    }

    fun getImageUrl(): String {
        return this.imageUrl
    }
}