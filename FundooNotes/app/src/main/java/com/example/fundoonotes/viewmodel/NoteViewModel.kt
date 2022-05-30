package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteService

class NoteViewModel(val noteService: NoteService): ViewModel() {

    private val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _getNoteStatus = MutableLiveData<ArrayList<Note>>()
    val getNoteStatus: LiveData<ArrayList<Note>> = _getNoteStatus

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus: LiveData<Boolean> = _gotoHomePageStatus


    fun saveNote(nTitle: String, nContent: String, noteID: String, time: String, priority: String) {
        noteService.saveNote(nTitle,nContent, noteID, time, priority){
            _noteStatus.value = it
        }
    }

    fun editNote(editNoteTitle: String, editNoteContent: String, noteID: String){
        noteService.editNote(editNoteTitle, editNoteContent, noteID){
            _noteStatus.value = it
        }
    }

    fun deleteNote(noteID: String){
        noteService.deleteNote(noteID){
            _noteStatus.value = it
        }
    }

    fun fetchNote() {
        noteService.getNoteFromFireStore() {
            _getNoteStatus.value = it
        }
    }

        fun setGotoHomePageStatus(status: Boolean){
        _gotoHomePageStatus.value = status
    }


}