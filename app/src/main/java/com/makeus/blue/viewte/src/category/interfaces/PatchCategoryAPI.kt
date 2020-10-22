package com.makeus.blue.viewte.src.category.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.category.models.RequestPatchCategory
import com.makeus.blue.viewte.src.category.models.ResponsePatchCategory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import java.util.concurrent.TimeUnit

interface PatchCategoryAPI {
    @PATCH("/user/categories")
    fun patchCategory(@Body params : RequestPatchCategory): Call<ResponsePatchCategory>

    companion object {

        fun create(): PatchCategoryAPI {
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
                .create(PatchCategoryAPI::class.java)
        }
    }
}