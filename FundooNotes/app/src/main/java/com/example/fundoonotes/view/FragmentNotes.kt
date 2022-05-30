package com.example.fundoonotes.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import android.content.Intent as Intent1


class FragmentNotes() : Fragment() {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel

    var noteID: String ?= null
    var title: String ?= null
    var content: String ?= null

    constructor(title: String, content: String, noteID: String) : this() {
        this.title = title
        this.content = content
        this.noteID = noteID
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val toolbar: androidx.appcompat.widget.Toolbar ?= view?.findViewById(R.id.toolbar1)

        val view: View = inflater.inflate(R.layout.fragment_notes, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton1)
        val editNoteBtn: FloatingActionButton = view.findViewById(R.id.editNoteFloatingBtn)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
        val closeNoteButton: FloatingActionButton = view.findViewById(R.id.closeNoteFloatingBtn)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        val contenttv: TextView = view.findViewById(R.id.noteContent)
        val titletv: TextView = view.findViewById(R.id.noteTitle)

        contenttv.setMovementMethod(ScrollingMovementMethod())

        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)

        contenttv.text = content
        titletv.text = title


        backButton.setOnClickListener{
            val intent: Intent1 = Intent1(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)

        }

        editNoteBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentEditNotes(title!!, content!!, noteID!!))
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        deleteButton.setOnClickListener {
            noteViewModel.deleteNote(noteID!!)
            Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show()

            val intent: android.content.Intent =
                android.content.Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        closeNoteButton.setOnClickListener {
            Toast.makeText(getContext(), "No changes made", Toast.LENGTH_SHORT).show()

            val intent: android.content.Intent =
                android.content.Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        return view
    }
}

