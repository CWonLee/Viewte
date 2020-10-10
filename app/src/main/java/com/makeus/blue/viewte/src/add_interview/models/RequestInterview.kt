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
    fun printAll() {
        println("categoryNo = $categoriesNo")
        println("i_title = $i_title")
        println("purpose = $purpose")
        println("date = $date")
        println("time = $time")
        println("location = $location")
        println("imageUrl = $imageUrl")

        for (i in questioninfo) {
            i.printAll()
        }
    }
}