package com.makeus.blue.viewte.src.add_interview.models

import com.google.gson.annotations.SerializedName

data class RequestInterviewQuestionInfo(
    @SerializedName("idx")
    private var idx: Int,

    @SerializedName("question")
    private var question: String,

    @SerializedName("answer")
    private var answer: String,

    @SerializedName("keyword")
    private var keyword: String
) {
    fun printAll() {
        println("idx = $idx")
        println("question = $question")
        println("answer = $answer")
        println("keyword = $keyword")
        println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ")
    }
}