package com.makeus.blue.viewte.src.is_add_question.models

import com.google.gson.annotations.SerializedName

data class RequestAddMemo(
    @SerializedName("memoinfo")
    private var memoinfo: ArrayList<RequestAddMemoInfo>
) {
}