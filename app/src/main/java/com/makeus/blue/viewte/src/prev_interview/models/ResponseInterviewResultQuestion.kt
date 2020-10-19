package com.makeus.blue.viewte.src.prev_interview.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseInterviewResultQuestion(
    @SerializedName("questionNo")
    private var questionNo: Int,

    @SerializedName("idx")
    private var idx: Int,

    @SerializedName("question")
    private var question: String,

    @SerializedName("answer")
    private var answer: String,

    @SerializedName("keyword")
    private var keyword: String
) : Serializable {
    fun getQuestionNo(): Int {
        return questionNo
    }

    fun getIdx(): Int {
        return idx
    }

    fun getQuestion(): String {
        return question
    }

    fun getAnswer(): String {
        return answer
    }

    fun getKeyword(): String {
        return keyword
    }
}