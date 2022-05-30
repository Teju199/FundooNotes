package com.example.fundoonotes.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.NoteService
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddNotes : Fragment() {

    lateinit var fstore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var noteID: String
    //lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel
    var priority : String = "Low"

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
        val redBtn: ImageView = view.findViewById(R.id.red)
        val blueBtn: ImageView = view.findViewById(R.id.blue)
        val yellowBtn : ImageView = view.findViewById(R.id.yellow)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)

        /*sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]*/


        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        redBtn.setOnClickListener {
            priority = "High"
            redBtn.setImageResource(R.drawable.ic_reddot_1_24)
            yellowBtn.setImageResource(0)
            blueBtn.setImageResource(0)
        }

        blueBtn.setOnClickListener {
            priority = "medium"
            blueBtn.setImageResource(R.drawable.ic_skybluedot_24)
            redBtn.setImageResource(0)
            yellowBtn.setImageResource(0)
        }

        yellowBtn.setOnClickListener {
            priority = "low"
            yellowBtn.setImageResource(R.drawable.ic_yellowdot_1_24)
            redBtn.setImageResource(0)
            blueBtn.setImageResource(0)
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

        val c: Calendar  = Calendar.getInstance()
        val df: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val time: String = df.format(c.getTime())

        if (nTitle.isEmpty() || nContent.isEmpty()) {
            Log.d(TAG, "Empty note cannot be added")
        }
        else {

            noteViewModel.saveNote(nTitle, nContent, noteID, time, priority)

            noteViewModel.noteStatus.observe(viewLifecycleOwner, Observer {
                if (it.status) {

                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()

                    noteViewModel.setGotoHomePageStatus(true)

                } else {
                    Toast.makeText(this.context, it.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }


}