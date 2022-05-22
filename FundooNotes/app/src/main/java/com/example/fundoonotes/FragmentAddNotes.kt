package com.example.fundoonotes

import android.content.ContentValues.TAG
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
import androidx.fragment.app.Fragment
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.DiskLruCache

class FragmentAddNotes : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_add_notes, container, false)

        val addContent: EditText = view.findViewById(R.id.addNoteContent)
        val addTitle: EditText = view.findViewById(R.id.addNoteTitle)
        val saveNoteButton: FloatingActionButton = view.findViewById(R.id.saveNoteFloatingBtn)
        val backButton : ImageView = view.findViewById(R.id.backButton1)

        val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()

        saveNoteButton.setOnClickListener {
            val nTitle = addTitle.getText().toString()
            val nContent = addContent.getText().toString()

            if(nTitle.isEmpty() || nContent.isEmpty()){
                Toast.makeText(getContext(), "Notes with empty fields cannot be saved",
                    Toast.LENGTH_LONG).show()
            }


            val documentReference: DocumentReference = fstore.collection("notes").document("noteID")
            val note: HashMap<String, String> = HashMap()
            note.put("title", nTitle)
            note.put("content", nContent)

            documentReference.set(note).addOnSuccessListener {
                Toast.makeText(getContext(), "Note added",
                    Toast.LENGTH_LONG).show()

                val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
            }
                .addOnFailureListener {
                    Toast.makeText(getContext(), "Failed to add note",
                        Toast.LENGTH_LONG).show()
                }
        }

        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        return view

    }

}