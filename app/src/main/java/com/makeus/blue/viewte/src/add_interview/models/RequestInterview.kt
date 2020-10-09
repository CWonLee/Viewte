package com.makeus.blue.viewte.src.add_interview.models

import com.google.gson.annotations.SerializedName

data class RequestInterview(
    @SerializedName("categoriesNo")
    private var categoriesNo: Int,

    @SerializedName("i_title")
    private var i_title: String,

    @SerializedName("purpose")
    private var purpose: String,

    @SerializedName("date")
    private var date: String,

    @SerializedName("time")
    private var time: String,

    @SerializedName("location")
    private var location: String,

    @SerializedName("imageUrl")
    private var imageUrl: String,

    @SerializedName("questioninfo")
    private var questioninfo: ArrayList<RequestInterviewQuestionInfo>
) {
    fun setCategoriesNo(categoriesNo: Int) {
        this.categoriesNo = categoriesNo
    }

    fun setITitle(i_title: String) {
        this.i_title = i_title
    }

    fun setPurpose(purpose: String) {
        this.purpose = purpose
    }

    fun setDate(date: String) {
        this.date = date
    }

    fun setTime(time: String) {
        this.time = time
    }

    fun setLocation(location: String) {
        this.location = location
    }

    fun setImageUrl(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun setQuestionInfo(questioninfo: ArrayList<RequestInterviewQuestionInfo>) {
        this.questioninfo = questioninfo
    }
}