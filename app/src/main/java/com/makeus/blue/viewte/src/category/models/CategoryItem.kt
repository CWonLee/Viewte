package com.makeus.blue.viewte.src.category.models

class CategoryItem(private var name: String, private var date: String, private var url: String) {
    fun getName(): String {
        return this.name
    }
    fun getDate(): String {
        return this.date
    }
    fun getUrl(): String {
        return this.url
    }
}