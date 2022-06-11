package com.example.fundoonotes.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteService
import com.example.fundoonotes.model.Utility
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class FragmentEditNotes() : Fragment() {

    lateinit var noteViewModel: NoteViewModel
    lateinit var title: String
    lateinit var content: String
    lateinit var noteID: String
    lateinit var dataBaseHelper: DataBaseHelper
    lateinit var editNoteTitle: EditText
    lateinit var editNoteContent: EditText
    lateinit var userID: String

    constructor(title:String, content: String, noteID: String): this(){
        this.title = title
        this.content = content
        this.noteID = noteID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_edit_notes, container, false)

        editNoteTitle = view.findViewById(R.id.editNoteTitle)
        editNoteContent = view.findViewById(R.id.editNoteContent)
        val saveEdtNoteFloatingBtn: FloatingActionButton = view.findViewById(R.id.saveEdtNoteFloatingBtn)
        val backButton: ImageView = view.findViewById(R.id.backButton1)
        dataBaseHelper = DataBaseHelper(context)
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val fstore = FirebaseFirestore.getInstance()

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)

        editNoteTitle.setText(title)
        editNoteContent.setText(content)

        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        saveEdtNoteFloatingBtn.setOnClickListener {

            var utility: Utility = Utility()
            val editNoteTitle1 = editNoteTitle.getText().toString()
            val editNoteContent1 = editNoteContent.getText().toString()

            if (utility.isNetworkAvailable(context).equals(true)) {
                editNoteFromSqlite(editNoteTitle1, editNoteContent1,noteID)
                editNoteFromFirestore(editNoteTitle1, editNoteContent1, userID, noteID)
            }
            else{
                editNoteFromSqlite(editNoteTitle1, editNoteContent1, noteID)
            }
        }
        return view
    }

    fun editNoteFromSqlite(updatedTitle: String, updatedContent: String, noteID: String) {
        if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {

            var note: Note = Note(updatedTitle, updatedContent, " ", noteID)

            dataBaseHelper.updateNote(updatedTitle, updatedContent, " ", noteID)
/*
            Toast.makeText(context, "Notes updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show()
        }*/
        }
    }

        fun editNoteFromFirestore(
            editNoteTitle: String,
            editNoteContent: String,
            userID: String,
            noteID: String
        ) {
            if (editNoteTitle.isEmpty() || editNoteContent.isEmpty()) {
                Toast.makeText(
                    getContext(), "Notes with empty fields cannot be saved",
                    Toast.LENGTH_LONG
                ).show()
            }

            noteViewModel.editNote(editNoteTitle, editNoteContent, userID, noteID)

            noteViewModel.noteStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

                if (it.status) {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()

                    noteViewModel.setGotoHomePageStatus(true)

                } else {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                }
            })
        }

}
