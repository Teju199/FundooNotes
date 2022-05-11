package com.example.fundoonotes

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView


class ActivityDashboard : AppCompatActivity() {

    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        val mProfileIcon: ImageView = findViewById(R.id.profileIcon)

        setSupportActionBar(toolbar)

        val nav: NavigationView = findViewById(R.id.navigationMenu)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)


        val noteLists: RecyclerView = findViewById(R.id.notesList)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open,
            R.string.close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)
        }

        mProfileIcon.setOnClickListener(View.OnClickListener {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentcontainer1,
                Fragment_dialog_userprofile()).commit()
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

}