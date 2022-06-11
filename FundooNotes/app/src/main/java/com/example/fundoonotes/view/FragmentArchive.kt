package com.example.fundoonotes.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentArchive: Fragment() {
    private lateinit var myAdapter: Adapter
    lateinit var notesViewList: RecyclerView
    lateinit var fstore: FirebaseFirestore
    lateinit var userID: String
    private lateinit var noteList: ArrayList<Note>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_archive, container, false)

        val backBtn : ImageView = view.findViewById(R.id.backButton7)

        fstore = FirebaseFirestore.getInstance()
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        notesViewList = view.findViewById(R.id.archivedNotes)
        noteList = ArrayList()

        displayArchivedNotes()

        backBtn.setOnClickListener {
            val intent: Intent = Intent(activity, ActivityDashboard::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun displayArchivedNotes() {
        fstore.collection("users").document(userID).collection("notes")
            .whereEqualTo("isArchive", true).get().addOnCompleteListener {
                for (document in it.result) {
                    val note: Note = Note()

                    note.content = document.data.get("Content").toString()
                    note.title = document.data.get("Title").toString()
                    note.userID = document.data.get("userID").toString()
                    note.noteID = document.data.get("noteID").toString()
                    note.time = document.data.get("time").toString()
                    note.priority = document.data.get("priority").toString()

                    noteList.add(note)

                }
                notesViewList.setLayoutManager(
                    StaggeredGridLayoutManager(
                        2,
                        StaggeredGridLayoutManager.VERTICAL
                    )
                )
                notesViewList.setHasFixedSize(true)
                val noteList1: ArrayList<Note> = ArrayList()
                noteList1.addAll(noteList)
                myAdapter = Adapter(noteList1, requireContext())
                notesViewList.adapter = myAdapter
                myAdapter.notifyDataSetChanged()
            }
    }
}