package com.makeus.blue.viewte.src.record_list.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.record_list.models.ResponseDeleteRecord
import com.makeus.blue.viewte.src.record_list.models.ResponseGetRecordList
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DeleteRecordAPI {
    @DELETE("/noise")
    fun deleteRecord(
        @Query("noiseNo") noiseNo: Int
    ): Call<ResponseDeleteRecord>

    companion object {

        fun create(): DeleteRecordAPI {
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
                .create(DeleteRecordAPI::class.java)
        }
    }
}