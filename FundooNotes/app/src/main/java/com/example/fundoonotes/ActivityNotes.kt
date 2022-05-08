package com.example.fundoonotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class ActivityNotes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val nav: NavigationView = findViewById(R.id.navigationMenu)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open,
            R.string.close
        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        toggle.setToolbarNavigationClickListener {
            drawerLayout.openDrawer(nav)
        }

    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when(item.itemId) {
                R.id.menu_notes -> {

                }

                R.id.menu_remainder ->{

                }

            }
            return true
        }


        /*nav.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener
        {
            when(it.itemId){

                R.id.menu_notes -> {

                }

                R.id.menu_remainder -> {

                }

                R.id.menu_label -> {

                }

                R.id.menu_addLabel -> {

                }

                R.id.menu_archive ->{

                }

                R.id.menu_trash -> {

                }

                R.id.menu_settings -> {

                }
            }
            return@OnNavigationItemSelectedListener false
        })*/




}