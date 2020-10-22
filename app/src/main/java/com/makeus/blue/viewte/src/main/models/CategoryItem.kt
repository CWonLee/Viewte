package com.makeus.blue.viewte.src.main.models

class CategoryItem(private var name: String, private var count: Int, private var minute: Int, private var categoryNo: Int, private var imageUrl: String?) {
    fun getName(): String {
        return this.name
    }
    fun getCount(): Int {
        return this.count
    }
    fun getMinute(): Int {
        return this.minute
    }
    fun getCategoryNo(): Int {
        return this.categoryNo
    }
    fun getImageUrl(): String? {
        return this.imageUrl
    }
}