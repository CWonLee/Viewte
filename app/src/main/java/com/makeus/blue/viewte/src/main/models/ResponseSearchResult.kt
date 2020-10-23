package com.makeus.blue.viewte.src.main.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ResponseSearchResult(
    @SerializedName("interviewNo")
    private var interviewNo: Int,

    @SerializedName("i_title")
    private var i_title: String,

    @SerializedName("imageUrl")
    private var imageUrl: String,

    @SerializedName("ismemo")
    private var ismemo: Char,

    @SerializedName("date")
    private var date: String,

    @SerializedName("week")
    private var week: Int,

    @SerializedName("categoriesNo")
    private var categoriesNo: Int
) : Serializable {
    fun getInterviewNo() : Int {
        return interviewNo
    }

    fun getITitle(): String {
        return i_title
    }

    fun getImageUrl() : String {
        return imageUrl
    }

    fun getIsMemo(): Char {
        return ismemo
    }

    fun getDate(): String {
        return date
    }

    fun getWeek(): Int {
        return week
    }

    fun getCategoriesNo(): Int {
        return categoriesNo
    }
}