package com.example.aiartmaker.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://ai-art-maker.p.rapidapi.com/"

    private val client = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader("x-rapidapi-key", "99ce56a1a0msha23dcce4e8f1a70p17f625jsn0e0d7f49bc37")
            .addHeader("x-rapidapi-host", "ai-art-maker.p.rapidapi.com")
            .build()
        chain.proceed(request)
    }.build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
