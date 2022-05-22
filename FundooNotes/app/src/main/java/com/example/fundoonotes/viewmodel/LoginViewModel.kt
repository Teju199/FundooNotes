package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.AuthListener
import com.example.fundoonotes.model.UserAuthService

class LoginViewModel(val userAuthService: UserAuthService) : ViewModel() {
    private val _loginStatus = MutableLiveData<AuthListener>()
    val loginStatus = _loginStatus as LiveData<AuthListener>

    fun userLogin(email: String, password: String){
        userAuthService.userLogin(email, password){
            if(it.status){
                _loginStatus.value = it
            }

        }
    }
}