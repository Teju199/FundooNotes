package com.example.fundoonotes

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundoonotes.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.DiskLruCache
import java.util.*
import kotlin.collections.HashMap

class FragmentEditNotes(val title: String, val content: String, var noteID: String) : Fragment() {

    lateinit var fstore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_edit_notes, container, false)

        val editNoteTitle: EditText = view.findViewById(R.id.editNoteTitle)
        val editNoteContent: EditText = view.findViewById(R.id.editNoteContent)
        val saveEdtNoteFloatingBtn: FloatingActionButton =
            view.findViewById(R.id.saveEdtNoteFloatingBtn)

        val fstore = FirebaseFirestore.getInstance()

        editNoteTitle.setText(title)
        editNoteContent.setText(content)

        saveEdtNoteFloatingBtn.setOnClickListener {

            val editNoteTitle = editNoteTitle.getText().toString()
            val editNoteContent = editNoteContent.getText().toString()

            if (editNoteTitle.isEmpty() || editNoteContent.isEmpty()) {
                Toast.makeText(
                    getContext(), "Notes with empty fields cannot be saved",
                    Toast.LENGTH_LONG
                ).show()
            }

            val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val documentReference: DocumentReference = fstore.collection("users")
                .document(userID).collection("notes").document(noteID)
            val note: HashMap<String, Any> = HashMap()

            note.put("Title", editNoteTitle)
            note.put("Content", editNoteContent)
            note.put("userID", userID)


            documentReference.update(note).addOnSuccessListener {
                Toast.makeText(
                    getContext(), "Note added",
                    Toast.LENGTH_LONG
                ).show()

                val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
                }
                    .addOnFailureListener {
                        Toast.makeText(
                            getContext(), "Failed to add note",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        return view
    }
}
