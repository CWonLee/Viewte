package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName

data class ResponseGetCategoryResult (
    @SerializedName("categoriesNo")
    private var categoriesNo: Int,

    @SerializedName("c_title")
    private var c_title: String,

    @SerializedName("imageUrl")
    private var imageUrl: String
) {
    fun getCategoriesNo(): Int {
        return categoriesNo
    }

    fun getC_title(): String {
        return c_title
    }

    fun getImageUrl(): String {
        return imageUrl
    }
}