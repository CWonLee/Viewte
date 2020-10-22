package com.makeus.blue.viewte.src.modify_memo.models

import com.google.gson.annotations.SerializedName

data class RequestPatchMemo(
    @SerializedName("memoNo")
    private var memoNo: Int,

    @SerializedName("memo")
    private var memo: String
) {

}