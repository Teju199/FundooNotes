package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.AuthListener
import com.example.fundoonotes.model.UserAuthService

class RegisterViewModel(val userAuthService: UserAuthService): ViewModel() {

    private val _registerStatus = MutableLiveData<AuthListener>()
    val registerStatus = _registerStatus as LiveData<AuthListener>

    fun registerUser(email: String, password: String){
        userAuthService.registerUser(email, password){
            if(it.status){
                _registerStatus.value = it
            }
        }
    }
}