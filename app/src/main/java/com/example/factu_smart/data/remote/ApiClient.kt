<<<<<<< HEAD

=======
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
package com.example.factu_smart.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteApi {

<<<<<<< HEAD
    private const val URL_BASE =
        "https://moxzojujhgzzpbzpxiix.supabase.co/rest/v1/"

    // ✅ TU NUEVA ANON KEY
    private const val API_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1veHpvanVqaGd6enBienB4aWl4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzczNDE1NDcsImV4cCI6MjA5MjkxNzU0N30.yov26r6OPj6ijeGLRr0h4BDr2mIBpR2InRA7x153md0"
=======
    private const val URL_BASE = "https://moxzojujhgzzpbzpxiix.supabase.co/rest/v1/"
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd

    val servicio: ServicioApi by lazy {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clienteHttp = OkHttpClient.Builder()
<<<<<<< HEAD

            .addInterceptor(logging)

            .addInterceptor { cadena ->

                val request = cadena.request()
                    .newBuilder()

                    .addHeader(
                        "apikey",
                        API_KEY
                    )

                    .addHeader(
                        "Authorization",
                        "Bearer $API_KEY"
                    )

                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )

                    .build()

                cadena.proceed(request)
            }

            .build()

        Retrofit.Builder()

            .baseUrl(URL_BASE)

            .client(clienteHttp)

            .addConverterFactory(
                GsonConverterFactory.create()
            )

            .build()

=======
            .addInterceptor(logging)
            .addInterceptor { cadena ->
                val request = cadena.request().newBuilder()
                    .addHeader("apikey", "sb_publishable_bVh2qxGK10uxcV_XRPWdsg_KK_hQAc0")
                    .addHeader("Authorization", "Bearer sb_publishable_bVh2qxGK10uxcV_XRPWdsg_KK_hQAc0")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build()
                cadena.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
>>>>>>> dfff2d5dcfcde442d00320649f823315317656dd
            .create(ServicioApi::class.java)
    }
}
