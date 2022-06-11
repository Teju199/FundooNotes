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
import com.example.fundoonotes.model.*
import com.example.fundoonotes.model.Dao.FirebaseDataLayer
import com.example.fundoonotes.model.Dao.SQLiteDataLayer
import com.example.fundoonotes.viewmodel.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class FragmentAddNotes : Fragment() {

    lateinit var fstore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var noteID: String
    lateinit var noteViewModel: NoteViewModel
    lateinit var nTitle: String
    lateinit var nContent: String
    lateinit var note: Note
    lateinit var userID: String
    var priority : String = "Low"
    var isArchive by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_add_notes, container, false)

        val addContent: EditText = view.findViewById(R.id.addNoteContent)
        val addTitle: EditText = view.findViewById(R.id.addNoteTitle)
        val saveNoteButton: FloatingActionButton = view.findViewById(R.id.saveNoteFloatingBtn)
        val backButton: ImageView = view.findViewById(R.id.backButton1)
        val redBtn: ImageView = view.findViewById(R.id.red)
        val blueBtn: ImageView = view.findViewById(R.id.blue)
        val yellowBtn : ImageView = view.findViewById(R.id.yellow)

        noteID = UUID.randomUUID().toString()
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val utility:Utility = Utility()

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)


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
            var flag : String = "false"
            val c: Calendar  = Calendar.getInstance()
            val df: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            nTitle = addTitle.text.toString()
            nContent = addContent.text.toString()
            val time: String = df.format(c.time)
            userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            isArchive = false

            if(utility.isNetworkAvailable(activity)) {
                var note: Note = Note(nTitle, nContent, userID, noteID, flag, time, priority, isArchive)
                saveNote(note)
                saveNoteToSqlLite(note)
            }
            else{
                flag = "true"
                var note: Note = Note(nTitle, nContent, userID, noteID, flag, time, priority, isArchive)
                saveNoteToSqlLite(note)
            }
        }
        return view
    }

    //saving note to Firestore
    fun saveNote(note: Note) {
        if (note.title.isEmpty() || note.content.isEmpty()) {
            Log.d(TAG, "Empty note cannot be added")
        }
        else {

            noteViewModel.saveNote(note)

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

    fun saveNoteToSqlLite(note:Note){
        val dataBaseHelper: DataBaseHelper = DataBaseHelper(requireActivity())
        val sqliteDataLayer  = SQLiteDataLayer()
        dataBaseHelper.addNote(note)
        Toast.makeText(context, "Note successfully added.", Toast.LENGTH_LONG).show()
    }

}