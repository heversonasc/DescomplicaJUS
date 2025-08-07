// File: com/example/jurisimplificado/viewmodel/RetrofitClient.kt
package com.example.jurisimplificado.data.network

import com.example.jurisimplificado.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
// IMPORT CORRIGIDO
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.groq.com/"

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.GROQ_API_KEY}")
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        // MUDANÇA PRINCIPAL: Usando o conversor do Gson
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: GroqApiService by lazy {
        retrofit.create(GroqApiService::class.java)
    }
}