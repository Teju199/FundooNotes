package com.example.fundoonotes.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FundooApi {
    @POST("./accounts:signInWithPassword?key=AIzaSyApwEtDEECwoz2tROxx5jsehd0AFv1mPLU")
    fun loginWithRestApi(@Body request: LoginRequest): Call<LoginResponse>
}