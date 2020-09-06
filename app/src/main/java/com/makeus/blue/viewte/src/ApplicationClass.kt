package com.makeus.blue.viewte.src

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.auth.KakaoSDK
import com.makeus.blue.viewte.src.kakao.KakaoSDKAdapter

class ApplicationClass : Application() {

    // JWT Token 값
    var X_ACCESS_TOKEN = "X-ACCESS-TOKEN"


    // SharedPreferences 키 값
    var TAG = "viewte"

    override fun onCreate() {
        prefs = AppPreferences(applicationContext)
        super.onCreate()

        instance = this

        if (sSharedPreferences == null) {
            sSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        }
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext(): ApplicationClass {
        checkNotNull(instance) { "this application does not inherit com.kakao.GlobalApplication" }
        return instance!!
    }

    companion object {
        var instance: ApplicationClass? = null

        lateinit var prefs : AppPreferences

        // 실서버 주소
        var BASE_URL = "http://15.165.78.22:8880"

        var sSharedPreferences: SharedPreferences? = null
    }
}