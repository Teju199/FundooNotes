package com.example.fundoonotes.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.FragmentCreateLabel
import com.example.fundoonotes.R
import com.example.fundoonotes.model.*
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import java.util.*


class ActivityDashboard() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var myAdapter: Adapter
    private lateinit var recyclerView: RecyclerView
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var view: String
    val listView = "LIST_VIEW"
    val gridView = "GRID_VIEW"
    lateinit var menuview: Menu
    lateinit var noteViewModel: NoteViewModel
    private var noteResults = ArrayList<Note>()
    private var noteList = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        val mAddNoteButton: FloatingActionButton = findViewById(R.id.addNoteFloatingBtn)
        val nav: NavigationView = findViewById(R.id.navigationMenu)
        recyclerView = findViewById(R.id.notesList)
        drawerLayout = findViewById(R.id.drawer)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)


        mAddNoteButton.setOnClickListener {
            getSupportFragmentManager().beginTransaction().add(
                R.id.fragmentcontainer1,
                FragmentAddNotes()
            ).commit()
        }

        mAddNoteButton.setOnClickListener {
            getSupportFragmentManager().beginTransaction().add(
                R.id.fragmentcontainer1,
                FragmentAddNotes()
            ).commit()
        }

        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setHomeButtonEnabled(true)


        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open,
            R.string.close
        )

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav.setNavigationItemSelectedListener(this)


        //Grid view
        view = gridView
        recyclerView.setLayoutManager(
            StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        )

        recyclerView.setHasFixedSize(true)

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)
            myAdapter.notifyDataSetChanged()
        }

        //calling function to fetch data
        getNotes()


        /*searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return true
            }
        })*/
    }


    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuview = menu!!
        menuInflater.inflate(R.menu.viewmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val toggleButton = menuview.getItem(0)

        when (item.itemId) {

            R.id.profile -> {
                getSupportFragmentManager().beginTransaction().add(
                    R.id.fragmentcontainer1,
                    FragmentDialogUserprofile()
                ).commit()
            }

            R.id.toggle -> {
                if (view == listView) {
                    view = gridView
                    recyclerView.setLayoutManager(
                        StaggeredGridLayoutManager(
                            2,
                            StaggeredGridLayoutManager.VERTICAL
                        )
                    )
                    recyclerView.setHasFixedSize(true)
                    //toggleButton.icon = ContextCompat.getDrawable(Activity(), R.drawable.ic_baseline_view_list_24)
                } else {
                    view = listView
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.setHasFixedSize(true)
                    //toggleButton.icon = ContextCompat.getDrawable(Activity(), R.drawable.ic_baseline_whitegrid_view_24)
                }
            }

            R.id.search -> {
                val searchView = item?.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d("onQueryTextChange", "query: " + newText)
                        myAdapter?.filter?.filter(newText)
                        return true
                    }
                })
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.menu_notes -> {
            }

            R.id.menu_remainder -> {

            }

            R.id.menu_label -> {

            }

            R.id.menu_addLabel -> {
                Toast.makeText(applicationContext, "Clicked on add Label", Toast.LENGTH_LONG).show()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentcontainer1, FragmentCreateLabel()).commit()
            }

            R.id.menu_archive -> {

            }

            R.id.menu_trash -> {

            }

            R.id.menu_settings -> {

            }

        }
        drawerLayout = findViewById(R.id.drawer)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //Function to fetch data from firestore

    private fun getNotes() {
        noteViewModel.fetchNote()

        noteViewModel.getNoteStatus.observe(this, androidx.lifecycle.Observer { noteLists ->
            if (noteLists.isNotEmpty()) {
                noteList.clear()

                for (note in noteLists) {
                    noteList.add(note)
                }
                noteResults.clear()
                noteResults.addAll(noteList)
                myAdapter = Adapter(noteList, this)
                recyclerView.adapter = myAdapter
            }

        })

        /*private fun filterList(newText: String) {
        val filteredList: MutableList<Note> = mutableListOf()
        for (singleNote in noteList) {
            if (singleNote.title.toLowerCase().contains(newText.toLowerCase(Locale.ROOT))
                || singleNote.content.toLowerCase().contains(newText.toLowerCase(Locale.ROOT))
            ) {
                filteredList.add(singleNote)
            }
        }
        myAdapter.filterList(filteredList)
    }*/
    }
}