package com.example.fundoonotes.model.Dao

import android.content.ContentValues.TAG
import android.util.Log
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class FirebaseDataLayer: NotesDataLayer() {

    var fstore : FirebaseFirestore = FirebaseFirestore.getInstance()
    var userID : String
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        userID = firebaseAuth.currentUser?.uid.toString()
    }

    override fun createNote(note1: Note, listener: ((NoteListener) -> Unit)?) {
        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(note1.noteID)
        val note: HashMap<String, Any> = HashMap()

        note.put("Title", note1.title)
        note.put("Content", note1.content)
        note.put("userID", userID)
        note.put("noteID", note1.noteID)
        note.put("flag", note1.flag)
        note.put("time", note1.time)
        note.put("priority", note1.priority)
        note.put("isArchive", note1.isArchive)

        documentReference.set(note).addOnSuccessListener {
            listener!!(NoteListener(true, "Note added"))
        }
            .addOnFailureListener {
                listener!!(NoteListener(false, "Failed to add note"))
            }
    }

    override fun deleteNote(noteID: String, listener: ((NoteListener) -> Unit)?) {
        fstore.collection("users").document(userID).collection("notes")
            .document(noteID).delete()
    }

    override fun getNote(listener:(ArrayList<Note>) -> Unit) {
        var noteList = ArrayList<Note>()

        fstore.collection("users").document(userID).collection("notes").get()
            .addOnCompleteListener {

                if (it.isSuccessful && it.result != null) {
                    for (document in it.result!!) {
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
                } else {
                    listener(ArrayList<Note>())
                    Log.d(TAG, "Error in fetching notes.")
                }
            }
    }

    override fun updateNote(editNoteTitle: String, editNoteContent: String, userID: String, noteID: String, listener: ((NoteListener) -> Unit)?) {
        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(noteID)
        val note: HashMap<String, Any> = HashMap()

        note.put("Title", editNoteTitle)
        note.put("Content", editNoteContent)
        note.put("userID", userID)

        documentReference.update(note).addOnSuccessListener {

            listener!!(NoteListener(true, "Note updated"))

        }
            .addOnFailureListener {

                listener!!(NoteListener(false, "Failed to update note"))
            }
    }
}