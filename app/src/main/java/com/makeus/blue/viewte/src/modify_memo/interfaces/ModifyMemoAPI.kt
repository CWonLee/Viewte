package com.makeus.blue.viewte.src.modify_memo.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.interview_detail.interfaces.GetMemoAPI
import com.makeus.blue.viewte.src.interview_detail.models.ResponseGetMemo
import com.makeus.blue.viewte.src.modify_memo.models.RequestPatchMemo
import com.makeus.blue.viewte.src.modify_memo.models.ResponsePatchMemo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ModifyMemoAPI {
    @PATCH("/question/memo")
    fun patchMemo(@Body params: RequestPatchMemo): Call<ResponsePatchMemo>

    companion object {

        fun create(): ModifyMemoAPI {
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
                .create(ModifyMemoAPI::class.java)
        }
    }
}