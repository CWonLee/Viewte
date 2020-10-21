package com.makeus.blue.viewte.src.is_add_question.models

import com.google.gson.annotations.SerializedName

data class RequestAddMemoInfo (
    @SerializedName("questionNo")
    private var questionNo: Int,

    @SerializedName("memo")
    private var memo: String
){

}