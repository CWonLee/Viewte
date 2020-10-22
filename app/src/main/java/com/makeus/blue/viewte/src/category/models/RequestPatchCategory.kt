package com.makeus.blue.viewte.src.category.models

import com.google.gson.annotations.SerializedName

data class RequestPatchCategory(
    @SerializedName("categoriesNo")
    private var categoriesNo: Int,

    @SerializedName("imageUrl")
    private var imageUrl: String
) {
}