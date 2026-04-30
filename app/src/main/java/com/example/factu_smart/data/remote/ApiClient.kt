package com.example.factu_smart.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {

    private const val URL_BASE = "https://moxzojujhgzzpbzpxiix.supabase.co/rest/v1/"

    val servicio: ServicioApi by lazy {

        // Agregamos un interceptor para ver los errores en el Logcat
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clienteHttp = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { cadena ->
                val request = cadena.request().newBuilder()
                    .addHeader("apikey", "sb_publishable_bVh2qxGK10uxcV_XRPWdsg_KK_hQAc0")
                    .addHeader("Authorization", "Bearer sb_publishable_bVh2qxGK10uxcV_XRPWdsg_KK_hQAc0")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal") // Ayuda a evitar errores de parsing en Supabase
                    .build()
                cadena.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServicioApi::class.java)
    }
}
