package com.example.fundoonotes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.view.Fragment_dialog_userprofile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList


class ActivityDashboard : AppCompatActivity() {

    lateinit var adapter: Adapter
    lateinit var fstore: FirebaseFirestore
    lateinit var title: String
    lateinit var content: String
    lateinit var noteList: MutableList<Note>
    private lateinit var myAdapter: Adapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        val mProfileIcon: ImageView = findViewById(R.id.profileIcon)

        setSupportActionBar(toolbar)

        val nav: NavigationView = findViewById(R.id.navigationMenu)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)
        val mAddNoteButton: FloatingActionButton = findViewById(R.id.addNoteFloatingBtn)
        val notesList: RecyclerView = findViewById(R.id.notesList)


        val fstore: FirebaseFirestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.notesList)

        recyclerView.setLayoutManager(StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL))

        notesList.setHasFixedSize(true)

        noteList = mutableListOf()
        myAdapter = Adapter(noteList)

        recyclerView.adapter = myAdapter

        eventChangeListener()

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open,
            R.string.close
        )

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        toolbar.setTitleTextColor(getResources().getColor(R.color.white))

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)
        }

        mProfileIcon.setOnClickListener(View.OnClickListener {
            getSupportFragmentManager().beginTransaction().add(
                R.id.fragmentcontainer1,
                Fragment_dialog_userprofile()
            ).commit()
        })

        mAddNoteButton.setOnClickListener {
            getSupportFragmentManager().beginTransaction().add(
                R.id.fragmentcontainer1,
                FragmentAddNotes()
            ).commit()
        }

    }

    private fun eventChangeListener() {
        fstore = FirebaseFirestore.getInstance()
        fstore.collection("notes")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            noteList.add(dc.document.toObject(Note::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_notes -> {

            }

            R.id.menu_remainder -> {

            }

            R.id.menu_label -> {

            }

            R.id.menu_addLabel -> {

            }

            R.id.menu_archive -> {

            }

            R.id.menu_trash -> {

            }

            R.id.menu_settings -> {

            }

        }
        return true
    }

    private fun getRandomColor(): Int {
        val colorCode: ArrayList<Int> = ArrayList()
        colorCode.add(R.color.blue)
        colorCode.add(R.color.skyblue)
        colorCode.add(R.color.grey)
        colorCode.add(R.color.lightGreen)
        colorCode.add(R.color.lightPurple)
        colorCode.add(R.color.pink)
        colorCode.add(R.color.yellow)

        val randomColor: Random = Random()
        val number: Int = randomColor.nextInt(colorCode.size)
        return colorCode.get(number)
    }

}


