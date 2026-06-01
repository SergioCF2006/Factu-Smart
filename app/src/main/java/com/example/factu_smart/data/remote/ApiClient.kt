package com.example.factu_smart.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {

    private const val URL_BASE =
        "https://moxzojujhgzzpbzpxiix.supabase.co/rest/v1/"

    private const val API_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1veHpvanVqaGd6enBienB4aWl4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzczNDE1NDcsImV4cCI6MjA5MjkxNzU0N30.yov26r6OPj6ijeGLRr0h4BDr2mIBpR2InRA7x153md0" // O publishable, NO secret

    val servicio: ServicioApi by lazy {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", API_KEY)
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .addHeader("Content-Type", "application/json")
                    .build()

                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioApi::class.java)
    }
}