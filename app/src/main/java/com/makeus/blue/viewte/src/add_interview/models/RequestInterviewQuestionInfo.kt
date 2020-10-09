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
    fun setIdx(idx: Int) {
        this.idx = idx
    }

    fun setQuestion(question: String) {
        this.question = question
    }

    fun setAnswer(answer: String) {
        this.answer = answer
    }

    fun setKeyword(keyword: String) {
        this.keyword = keyword
    }
}