package com.example.fundoonotes.view

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fundoonotes.R
import com.example.fundoonotes.model.*
import com.example.fundoonotes.model.Dao.FirebaseDataLayer
import com.example.fundoonotes.viewmodel.NoteViewModel
import com.example.fundoonotes.viewmodel.NoteViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlin.properties.Delegates


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
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var searchView: SearchView
    lateinit var fragmentAddNotes: FragmentAddNotes
    lateinit var allNotes: ArrayList<Note>
    lateinit var dataBaseHelper: DataBaseHelper
    lateinit var progressBar: ProgressBar
    var isScrolling = false
    var currentItems by Delegates.notNull<Int>()
    var totalItems by Delegates.notNull<Int>()
    var scrollOutItems by Delegates.notNull<Int>()
    lateinit var manager: RecyclerView.LayoutManager
    lateinit var firebaseDataLayer: FirebaseDataLayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        val mAddNoteButton: FloatingActionButton = findViewById(R.id.addNoteFloatingBtn)
        val nav: NavigationView = findViewById(R.id.navigationMenu)
        fragmentAddNotes = FragmentAddNotes()
        recyclerView = findViewById(R.id.notesList)
        drawerLayout = findViewById(R.id.drawer)
        progressBar = findViewById(R.id.progressbar)
        searchView = findViewById(R.id.searchIcon)
        firebaseDataLayer = FirebaseDataLayer()

        dataBaseHelper = DataBaseHelper(this)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(NoteService()))
            .get(NoteViewModel::class.java)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]


        mAddNoteButton.setOnClickListener {
            supportFragmentManager.beginTransaction().add(
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

        view = listView

        manager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(manager)

        recyclerView.setHasFixedSize(true)

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)
            myAdapter.notifyDataSetChanged()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = manager.childCount
                totalItems = manager.itemCount


                scrollOutItems = (manager as LinearLayoutManager).findFirstVisibleItemPosition()

                if(isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false
                    fetchData()
                }
            }
        })

        //calling function to fetch data
        var utility: Utility = Utility()

        if(utility.isNetworkAvailable(this).equals(true)) {
            getNotesFromSqlite()
            getNotesFromFirestore()
        }else{
            getNotesFromSqlite()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("onQueryTextChange", "query: " + newText)
                noteViewModel.setQueryText(newText!!)
                noteViewModel.queryText.observe(this@ActivityDashboard, androidx.lifecycle.Observer {
                    myAdapter.filter.filter(newText!!)
                })

                return true
            }
        })

    }

    private fun fetchData() {
        progressBar.setVisibility(View.VISIBLE)
        Handler().postDelayed(Runnable(){
            getNotesFromFirestore()}, 5000)
        progressBar.setVisibility(View.GONE )
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
                } else {
                    view = listView
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.setHasFixedSize(true)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.menu_notes -> {
            }

            R.id.menu_remainder -> {
                //myWorkManager()
            }

            R.id.menu_label -> {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentcontainer1, FragmentLabel()).commit()
            }

            R.id.menu_addLabel -> {
                Toast.makeText(applicationContext, "Clicked on add Label", Toast.LENGTH_LONG).show()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentcontainer1, FragmentCreateLabel()).commit()
            }

            R.id.menu_archive -> {
                Toast.makeText(applicationContext, "Clicked on add Label", Toast.LENGTH_LONG).show()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentcontainer1, FragmentArchive()).commit()
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

    fun getNotesFromFirestore() {
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
                myAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun getNotesFromSqlite() {

        progressBar.setVisibility(View.GONE)
        allNotes = dataBaseHelper.getAll()
        noteResults = allNotes
        myAdapter = Adapter(allNotes, this)
        recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()

    }
}