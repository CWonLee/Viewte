package com.makeus.blue.viewte.src.prev_interview.models

import com.google.gson.annotations.SerializedName

data class ResponseInterviewResult(
    @SerializedName("Interview")
    private var Interview: ArrayList<ResponseInterviewResultInterview>,

    @SerializedName("question")
    private var question: ArrayList<ResponseInterviewResultQuestion>
) {
    fun getInterviewList(): ArrayList<ResponseInterviewResultInterview> {
        return Interview
    }

    fun getQuestionList(): ArrayList<ResponseInterviewResultQuestion> {
        return question
    }
}