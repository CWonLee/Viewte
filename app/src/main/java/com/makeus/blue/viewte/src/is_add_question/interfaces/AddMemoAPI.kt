package com.makeus.blue.viewte.src.is_add_question.interfaces

import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.interview.interfaces.QuestionAPI
import com.makeus.blue.viewte.src.interview.models.RequestQuestion
import com.makeus.blue.viewte.src.interview.models.ResponseQuestion
import com.makeus.blue.viewte.src.is_add_question.models.RequestAddMemo
import com.makeus.blue.viewte.src.is_add_question.models.ResponseAddMemo
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AddMemoAPI {
    @POST("/question/memo")
    fun postAddMemo(
        @Body requestAddMemo: RequestAddMemo
    ): Call<ResponseAddMemo>

    companion object {

        fun create(): AddMemoAPI {
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
                .create(AddMemoAPI::class.java)
        }
    }
}