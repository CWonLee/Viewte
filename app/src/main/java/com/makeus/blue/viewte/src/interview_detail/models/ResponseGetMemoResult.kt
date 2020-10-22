package com.makeus.blue.viewte.src.interview_detail.models

import com.google.gson.annotations.SerializedName

data class ResponseGetMemoResult(
    @SerializedName("questionNo")
    private var questionNo: Int,

    @SerializedName("question")
    private var question: String,

    @SerializedName("memoNo")
    private var memoNo: Int,

    @SerializedName("memo")
    private var memo: String
) {
    fun getQuestionNo(): Int {
        return questionNo
    }

    fun getQuestion(): String {
        return question
    }

    fun getMemoNo(): Int {
        return memoNo
    }

    fun getMemo(): String {
        return memo
    }
}