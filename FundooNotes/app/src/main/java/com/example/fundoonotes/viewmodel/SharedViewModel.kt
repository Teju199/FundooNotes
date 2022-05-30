package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteService
import com.example.fundoonotes.model.UserAuthService
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class SharedViewModel(val userAuthService: UserAuthService): ViewModel() {

    val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val noteService: NoteService = NoteService()

    private val _gotoLoginPageStatus = MutableLiveData<Boolean>()
    val gotoLoginPageStatus: LiveData<Boolean> = _gotoLoginPageStatus

    private val _gotoRegistrationPageStatus = MutableLiveData<Boolean>()
    val gotoRegistrationPageStatus: LiveData<Boolean> = _gotoRegistrationPageStatus

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus: LiveData<Boolean> = _gotoHomePageStatus

    /*private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _getNoteStatus = MutableLiveData<NoteListener>()
    val getNoteStatus: LiveData<NoteListener> = _getNoteStatus*/

    private val _queryText = MutableLiveData<String>()
    val queryText: LiveData<String> = _queryText


    fun setGotoLoginPageStatus(status: Boolean){
        _gotoLoginPageStatus.value = status
    }

    fun setGotoRegistrationPageStatus(status: Boolean){
        _gotoRegistrationPageStatus.value = status
    }

    fun setGotoHomePageStatus(status: Boolean){
        _gotoHomePageStatus.value = status
    }

    /*fun fetchNote(){
        noteService.getNoteFromFireStore(){
            _getNoteStatus.value = it
        }

    }*/

    fun setQueryText(text: String){
        _queryText.value = text
    }
}

