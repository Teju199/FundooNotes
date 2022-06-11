package com.example.fundoonotes.view

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
//import com.example.fundoonotes.DataBaseHelper
import com.example.fundoonotes.R
import com.example.fundoonotes.model.*
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap
import kotlin.properties.Delegates
import android.content.Intent as Intent1


class FragmentNotes() : Fragment() {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var noteViewModel: NoteViewModel
    lateinit var dataBaseHelper: DataBaseHelper
    lateinit var userID: String
    lateinit var note: Note
    var isArchive by Delegates.notNull<Boolean>()

    constructor(note: Note) : this(){
        this.note = note
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        val archive: ImageView = view.findViewById(R.id.archive)
        val reminder: ImageView = view.findViewById(R.id.reminder)


        //fragmentRemainderDialog = FragmentRemainderDialog()
        //dateTimeView = view.findViewById(R.id.dateTime)
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        isArchive = false

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)
        dataBaseHelper = DataBaseHelper(context)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        val contenttv: TextView = view.findViewById(R.id.noteContent)
        val titletv: TextView = view.findViewById(R.id.noteTitle)

        contenttv.setMovementMethod(ScrollingMovementMethod())

        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)

        contenttv.text = note.content
        titletv.text = note.title


        backButton.setOnClickListener{
            val intent: Intent1 = Intent1(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)

        }

        editNoteBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentEditNotes(note.title, note.content, note.noteID))
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        deleteButton.setOnClickListener {
            var utility: Utility = Utility()
            if (utility.isNetworkAvailable(context).equals(true)) {
                noteViewModel.deleteNote(note.noteID)
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show()

                deleteNoteFromSqlite()

                val intent: android.content.Intent =
                    android.content.Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
            }

            else{
                deleteNoteFromSqlite()
                val intent: android.content.Intent =
                    android.content.Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
            }
        }

        archive.setOnClickListener {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            isArchive = true
            val documentReference: DocumentReference = FirebaseFirestore.getInstance().collection("users")
                .document(userID).collection("notes").document(note.noteID)
            var note1: HashMap<String, Any> = HashMap()

            note1.put("Title", note.title)
            note1.put("Content", note.content)
            note1.put("userID", userID)
            note1.put("noteID", note.noteID)
            note1.put("flag", note.flag)
            note1.put("time", note.time)
            note1.put("priority", note.priority)
            note1.put("isArchive", isArchive)

            documentReference.update(note1)

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

        reminder.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentRemainderDialog(note.content, note.title))
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        return view
    }

    private fun deleteNoteFromSqlite() {
        dataBaseHelper.deleteNote(note.noteID)
    }

}

