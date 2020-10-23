package com.makeus.blue.viewte.src

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    val TAG = "viewte"
    val JWT = "X_ACCESS_TOKEN"
    val ICE_BREAK = "ICE_BREAK"
    val prefs: SharedPreferences = context.getSharedPreferences(TAG, 0)

    var myEditText: String?
        get() = prefs.getString(JWT, "")
        set(value) = prefs.edit().putString(JWT, value).apply()

    var myIceBreak: String?
        get() = prefs.getString(ICE_BREAK, "")
        set(value) = prefs.edit().putString(ICE_BREAK, value).apply()
}