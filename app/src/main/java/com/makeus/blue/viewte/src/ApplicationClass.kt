package com.makeus.blue.viewte.src

import android.app.Application
import com.kakao.auth.KakaoSDK
import com.makeus.blue.viewte.src.kakao.KakaoSDKAdapter

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
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
    }
}