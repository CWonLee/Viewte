package com.makeus.blue.viewte.src.interview_detail.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.category.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.category.models.ResponseInterview
import com.makeus.blue.viewte.src.interview_detail.models.ResponseGetMemo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface GetMemoAPI {
    @GET("/question/{questionNo}/memo")
    fun getMemo(@Path("questionNo") questionNo: Int): Call<ResponseGetMemo>

    companion object {

        fun create(): GetMemoAPI {
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
                .baseUrl(ApplicationClass.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GetMemoAPI::class.java)
        }
    }
}