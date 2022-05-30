package com.example.fundoonotes.model

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import java.util.*
import kotlin.collections.HashMap

class NoteService {

    lateinit var noteID: String
    lateinit var fstore : FirebaseFirestore
    lateinit var userID : String
    lateinit var firebaseAuth: FirebaseAuth

    init {
        fstore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        userID = firebaseAuth.currentUser?.uid.toString()
    }

    fun saveNote(nTitle: String, nContent: String, noteID: String, time: String, priority: String, listener: (NoteListener) -> Unit) {

        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(noteID)
        val note: HashMap<String, Any> = HashMap()

        note.put("Title", nTitle)
        note.put("Content", nContent)
        note.put("userID", userID)
        note.put("noteID", noteID)
        note.put("time", time)
        note.put("priority", priority)

        documentReference.set(note).addOnSuccessListener {
            listener(NoteListener(true, "Note added"))
        }
            .addOnFailureListener {
                listener(NoteListener(false, "Failed to add note "))
            }
    }

    fun editNote(editNoteTitle: String, editNoteContent: String, noteID: String, listener:(NoteListener) -> Unit) {

        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(noteID)
        val note: HashMap<String, Any> = HashMap()

        note.put("Title", editNoteTitle)
        note.put("Content", editNoteContent)
        note.put("userID", userID)


        documentReference.update(note).addOnSuccessListener {

            listener(NoteListener(true, "Note updated"))

        }
            .addOnFailureListener {

                listener(NoteListener(false, "Failed to update note"))
            }
    }

    fun deleteNote(noteID: String, listener:(NoteListener) -> Unit){
        fstore.collection("users").document(userID).collection("notes")
            .document(noteID).delete()
    }

    fun getNoteFromFireStore(listener:(ArrayList<Note>) -> Unit){

        var noteList = ArrayList<Note>()

        fstore.collection("users").document(userID).collection("notes").get()
            .addOnCompleteListener {

                if(it.isSuccessful && it.result != null){
                    for(document in it.result!!){
                        var note: Note = Note()

                        note.content = document.data.get("Content").toString()
                        note.title = document.data.get("Title").toString()
                        note.userID = document.data.get("userID").toString()
                        note.noteID = document.data.get("noteID").toString()
                        note.time = document.data.get("time").toString()
                        note.priority = document.data.get("priority").toString()

                        noteList.add(note)
                    }
                    listener(noteList)
                }else{

                    listener(ArrayList<Note>())
                    Log.d(TAG, "Error in fetching notes.")
                }

                /*if(it.isSuccessful()){
                    for(document in it.result){
                        var note: Note = Note()

                        note.content = document.data.get("Content").toString()
                        note.title = document.data.get("Title").toString()
                        note.userID = document.data.get("userID").toString()
                        note.noteID = document.data.get("noteID").toString()
                        note.time = document.data.get("time").toString()
                        note.priority = document.data.get("priority").toString()

                        noteList.add(note)
                    }

                    listener(NoteListener(true, "Notes fetched successfully"))

                }else{
                    listener(NoteListener(false, "Notes fetched successfully"))
                }*/
            }
    }

}