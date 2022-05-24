package com.example.fundoonotes

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.DiskLruCache
import java.security.AccessController.getContext
import java.util.*
import kotlin.collections.HashMap

class FragmentAddNotes : Fragment() {

    lateinit var fstore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var noteID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_add_notes, container, false)

        val addContent: EditText = view.findViewById(R.id.addNoteContent)
        val addTitle: EditText = view.findViewById(R.id.addNoteTitle)
        val saveNoteButton: FloatingActionButton = view.findViewById(R.id.saveNoteFloatingBtn)
        val backButton: ImageView = view.findViewById(R.id.backButton1)
        val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        saveNoteButton.setOnClickListener {
            saveNote(addTitle, addContent, fstore)
        }
        return view
    }

    //saving note to Firestore
    fun saveNote(addTitle: EditText, addContent: EditText, fstore: FirebaseFirestore) {

        val nTitle = addTitle.getText().toString()
        val nContent = addContent.getText().toString()
        noteID = UUID.randomUUID().toString()
        val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (nTitle.isEmpty() || nContent.isEmpty()) {
            Log.d(TAG, "Empty note cannot be added")
        }

        val documentReference: DocumentReference = fstore.collection("users")
            .document(userID).collection("notes").document(noteID)
        val note: HashMap<String, Any> = HashMap()

        note.put("Title", nTitle)
        note.put("Content", nContent)
        note.put("userID", userID)
        note.put("noteID", noteID)

        documentReference.set(note).addOnSuccessListener {
            Toast.makeText(
                getContext(), "Note added",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }
            .addOnFailureListener {
                Toast.makeText(
                    getContext(), "Failed to add note",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

}