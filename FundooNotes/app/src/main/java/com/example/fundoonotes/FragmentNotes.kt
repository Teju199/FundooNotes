package com.example.fundoonotes

import android.app.Activity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent as Intent1


class FragmentNotes(var title: String, var content: String, var noteID : String) : Fragment() {

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
                transaction.replace(R.id.fragmentcontainer1, FragmentEditNotes(title, content, noteID))
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        deleteButton.setOnClickListener {
            deleteNote(noteID)
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

    /*override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.noteoptionsmenu, menu)
        Toast.makeText(getContext(), "clicked on menu", Toast.LENGTH_SHORT).show()

        val close = menu.findItem(R.id.action_close)

        close.setOnMenuItemClickListener {
            Toast.makeText(getContext(), "clicked on close", Toast.LENGTH_SHORT).show()
            val intent: Intent1 =
                Intent1(getActivity(), ActivityDashboard::class.java)
            this.startActivity(intent)
            true
        }
        super.onCreateOptionsMenu(menu,menuInflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(getContext(), "menu selected", Toast.LENGTH_SHORT).show()

        when(item.itemId){

            R.id.action_delete -> {
                Toast.makeText(getContext(), "clicked on delete", Toast.LENGTH_SHORT).show()
            }

            *//*R.id.action_close -> {
                val intent: android.content.Intent =
                    android.content.Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
            }*//*

        }
        return super.onOptionsItemSelected(item)
    }*/

    private fun deleteNote(noteID: String) {
        val fstore = FirebaseFirestore.getInstance()
        val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        fstore.collection("users").document(userID).collection("notes")
            .document(noteID).delete()
    }

}

