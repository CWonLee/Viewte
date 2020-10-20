package com.makeus.blue.viewte.src.interview.models

import com.google.gson.annotations.SerializedName

data class RequestQuestionInfo(
    @SerializedName("question")
    private var question: String,

    @SerializedName("answer")
    private var answer: String
) {
    fun setQuestion(param : String) {
        this.question = param
    }

    fun setAnswer(param: String) {
        this.answer = param
    }

    fun printlnData() {
        println("question : $question, answer + $answer")
    }
}