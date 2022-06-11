package com.example.fundoonotes.api

import com.example.fundoonotes.model.AuthListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginLoader {
    fun getLoginDone(listener: (LoginListener), email: String, password: String){
        Client().getInstance()?.funGetMyApi()?.loginWithRestApi(LoginRequest(email,password, returnSecureToken = true))
            ?.enqueue(object: Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        listener.getLoginDone(response.body(), true, "Login successful")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    listener.getLoginDone(null, false, t.message.toString())
                }

            })

    }
}