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
import com.example.fundoonotes.model.NoteService
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class FragmentEditNotes() : Fragment() {

    lateinit var fstore: FirebaseFirestore
    //lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel

    lateinit var title: String
    lateinit var content: String
    lateinit var noteID: String

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

        val editNoteTitle: EditText = view.findViewById(R.id.editNoteTitle)
        val editNoteContent: EditText = view.findViewById(R.id.editNoteContent)
        val saveEdtNoteFloatingBtn: FloatingActionButton = view.findViewById(R.id.saveEdtNoteFloatingBtn)
        val backButton: ImageView = view.findViewById(R.id.backButton1)

        val fstore = FirebaseFirestore.getInstance()

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)

        /*sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]*/

        editNoteTitle.setText(title)
        editNoteContent.setText(content)

        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        saveEdtNoteFloatingBtn.setOnClickListener {

            val editNoteTitle = editNoteTitle.getText().toString()
            val editNoteContent = editNoteContent.getText().toString()

            if (editNoteTitle.isEmpty() || editNoteContent.isEmpty()) {
                Toast.makeText(
                    getContext(), "Notes with empty fields cannot be saved",
                    Toast.LENGTH_LONG
                ).show()
            }

            noteViewModel.editNote(editNoteTitle, editNoteContent, noteID)

            noteViewModel.noteStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer{

                if (it.status) {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()

                    noteViewModel.setGotoHomePageStatus(true)

                } else {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                }
            })

            }
        return view
    }
}
