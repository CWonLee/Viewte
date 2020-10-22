package com.makeus.blue.viewte.src.setting.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.setting.models.RequestPatchUser
import com.makeus.blue.viewte.src.setting.models.ResponsePatchUser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface PatchUserAPI {
    @PATCH("/user/info")
    fun patchUser(
        @Body requestPatchUser: RequestPatchUser
    ): Call<ResponsePatchUser>

    companion object {

        fun create(): PatchUserAPI {
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
                .create(PatchUserAPI::class.java)
        }
    }
}