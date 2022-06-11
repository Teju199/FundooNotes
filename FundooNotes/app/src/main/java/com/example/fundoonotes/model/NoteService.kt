package com.example.fundoonotes.model

import com.example.fundoonotes.model.Dao.FirebaseDataLayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NoteService {

    var fstore: FirebaseFirestore
    var userID: String
    var firebaseAuth: FirebaseAuth
    var firebaseDataLayer: FirebaseDataLayer

    init {
        fstore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser?.uid.toString()
        firebaseDataLayer = FirebaseDataLayer()
    }

    fun saveNote(note1: Note, listener: (NoteListener) -> Unit) {
        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(note1.noteID)

        firebaseDataLayer.createNote(note1, listener)
    }

    fun editNote(
        editNoteTitle: String,
        editNoteContent: String,
        userID: String,
        noteID: String,
        listener: (NoteListener) -> Unit
    ) {

        firebaseDataLayer.updateNote(editNoteTitle, editNoteContent, userID, noteID, listener)
    }

    fun deleteNote(noteID: String, listener: (NoteListener) -> Unit) {
        firebaseDataLayer.deleteNote(noteID, listener)
    }

    fun getNoteFromFireStore(listener: (ArrayList<Note>) -> Unit) {
        firebaseDataLayer.getNote(listener)
    }

}