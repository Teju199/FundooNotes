package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.Dao.FirebaseDataLayer
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.model.NoteService

class NoteViewModel(): ViewModel() {

    lateinit var noteService: NoteService

    constructor(noteService: NoteService): this(){
        this.noteService = noteService
    }

    val _noteStatus = MutableLiveData<NoteListener>()
    val noteStatus: LiveData<NoteListener> = _noteStatus

    private val _getNoteStatus = MutableLiveData<ArrayList<Note>>()
    val getNoteStatus: LiveData<ArrayList<Note>> = _getNoteStatus

    private val _queryText = MutableLiveData<String>()
    val queryText: LiveData<String> = _queryText

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus: LiveData<Boolean> = _gotoHomePageStatus


    fun saveNote(note1: Note) {
        noteService.saveNote(note1){
            _noteStatus.value = it
        }
    }

    fun editNote(editNoteTitle: String, editNoteContent: String, userID: String, noteID: String){
        noteService.editNote(editNoteTitle, editNoteContent,userID, noteID){
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

    fun setQueryText(text: String){
        _queryText.value = text
    }

        fun setGotoHomePageStatus(status: Boolean){
        _gotoHomePageStatus.value = status
    }


}