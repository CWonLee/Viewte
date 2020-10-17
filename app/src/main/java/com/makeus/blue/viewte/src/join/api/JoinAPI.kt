package com.makeus.blue.viewte.src.join.api

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.ApplicationClass.Companion.BASE_URL
import com.makeus.blue.viewte.src.join.models.RequestJoin
import com.makeus.blue.viewte.src.join.models.ResponseJoin
import com.makeus.blue.viewte.src.login.api.LoginAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface JoinAPI {
    @POST("/user/signUp")
    fun postJoin(
        @Body requestLogin: RequestJoin
    ): Call<ResponseJoin>

    companion object {

        fun create(): JoinAPI {
            val jwtToken = ApplicationClass.prefs.myEditText

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-ACCESS-TOKEN", jwtToken.toString())
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JoinAPI::class.java)
        }
    }
}