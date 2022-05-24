package com.example.fundoonotes.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.User
import com.example.fundoonotes.model.UserAuthService
import com.google.firebase.firestore.FirebaseFirestore

class SharedViewModel(val userAuthService: UserAuthService): ViewModel() {

    val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _gotoLoginPageStatus = MutableLiveData<Boolean>()
    val gotoLoginPageStatus: LiveData<Boolean> = _gotoLoginPageStatus

    private val _gotoRegistrationPageStatus = MutableLiveData<Boolean>()
    val gotoRegistrationPageStatus: LiveData<Boolean> = _gotoRegistrationPageStatus

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus: LiveData<Boolean> = _gotoHomePageStatus

    fun setGotoLoginPageStatus(status: Boolean){
        _gotoLoginPageStatus.value = status
    }

    fun setGotoRegistrationPageStatus(status: Boolean){
        _gotoRegistrationPageStatus.value = status
    }

    fun setGotoHomePageStatus(status: Boolean){
        _gotoHomePageStatus.value = status
    }
}

