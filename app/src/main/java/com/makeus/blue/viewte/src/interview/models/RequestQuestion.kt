package com.makeus.blue.viewte.src.interview.models

import com.google.gson.annotations.SerializedName

data class RequestQuestion(
    @SerializedName("interviewNo")
    private var interviewNo: Int,

    @SerializedName("questioninfo")
    private var questioninfo: ArrayList<RequestQuestionInfo>
) {
    fun setInterviewNo(param : Int) {
        this.interviewNo = param
    }

        fun setQuestionInfo(param : ArrayList<RequestQuestionInfo>) {
        this.questioninfo = param
    }
}