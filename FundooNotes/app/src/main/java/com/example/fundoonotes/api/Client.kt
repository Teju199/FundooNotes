package com.example.fundoonotes.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class Client() {
    private lateinit var fundooApi: FundooApi
    private var fundooClientInstance: Client ?= null
    private lateinit var okHttpClient: OkHttpClient

    init {
        var httpClient = OkHttpClient.Builder()
        okHttpClient = httpClient.connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS)
            .build()
    }

    fun getInstance(): Client?{
        if(fundooClientInstance == null){
            fundooClientInstance = Client()
        }
        return fundooClientInstance
    }

    fun funGetMyApi(): FundooApi{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()

        fundooApi = retrofit.create(FundooApi::class.java)

        return fundooApi
    }
}