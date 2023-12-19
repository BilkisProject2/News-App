package com.example.newsapp.API

import com.example.newsapp.Util.Contants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstances
{
    companion object{

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            var client = OkHttpClient.Builder().addInterceptor(logging)
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}