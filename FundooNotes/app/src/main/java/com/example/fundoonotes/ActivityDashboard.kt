package com.example.fundoonotes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.view.Fragment_dialog_userprofile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*



class ActivityDashboard() : AppCompatActivity() {

    lateinit var fstore: FirebaseFirestore
    lateinit var noteList: MutableList<Note>
    private lateinit var myAdapter: Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userID: String

    @SuppressLint("ResourceAsColor")
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
        //val menuItem: MenuItem = menu!!.findItem(R.id.searchIcon)
        //val searchView: SearchView = MenuItemCompat.getActionView(menuItem) as SearchView
        val searchView: SearchView = findViewById(R.id.searchIcon)

        recyclerView = findViewById(R.id.notesList)

        //Grid view
        recyclerView.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))

        notesList.setHasFixedSize(true)
        noteList = mutableListOf()

        //sending noteList to Adapter
        myAdapter = Adapter(noteList)
        recyclerView.adapter = myAdapter

        //Navigation Drawer setup
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open,
            R.string.close)

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        toolbar.setTitleTextColor(getResources().getColor(R.color.white))

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)

            myAdapter.notifyDataSetChanged()
        }

        //calling function to fetch data
        getUserData(noteList)

        //profile icon click
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

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(newText: String) {
        val filteredList: MutableList<Note> = mutableListOf()
        for(singleNote in noteList){
            if(singleNote.title.toLowerCase().contains(newText.toLowerCase(Locale.ROOT))
                || singleNote.content.toLowerCase().contains(newText.toLowerCase(Locale.ROOT))){
                filteredList.add(singleNote)
            }
        }
        myAdapter.filterList(filteredList)
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

    //Function to fetch data from firestore
    fun getUserData(noteList: MutableList<Note>){
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        fstore = FirebaseFirestore.getInstance()
        lateinit var userNotes: ArrayList<Note>
        fstore.collection("users").document(userID).collection("notes").get()
            .addOnCompleteListener {

                noteList.clear()

            if(it.isSuccessful()){
                for(document in it.result){
                    val note: Note = Note()

                    note.content = document.data.get("Content").toString()
                    note.title = document.data.get("Title").toString()
                    note.userID = document.data.get("userID").toString()
                    note.noteID = document.data.get("noteID").toString()

                    noteList.add(note)
                }
                myAdapter.notifyDataSetChanged()

            }else{
                Log.w(TAG, "Error fetching documents")
            }
        }

    }

}

