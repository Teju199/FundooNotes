package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.view.ActivityDashboard
import com.example.fundoonotes.view.FragmentLogin
import com.example.fundoonotes.view.FragmentRegister
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var fragment: Fragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        addFragment()

        fun observeAppNav() {

            sharedViewModel.gotoLoginPageStatus.observe(this) {
                if (it == true) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentcontainer, FragmentLogin()).addToBackStack(null).commit()
                    }
                }
            }

            sharedViewModel.gotoRegistrationPageStatus.observe(this) {
                if (it == true) {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentcontainer, FragmentRegister()).addToBackStack(null).commit()
                    }
                }
            }

            sharedViewModel.gotoHomePageStatus.observe(this) {
                if (it == true) {
                    val intent: Intent = Intent(this, ActivityDashboard::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun addFragment() {
        fragment = FragmentLogin()
        fragmentManager = getSupportFragmentManager()
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentcontainer, FragmentLogin())
        fragmentTransaction.commit()
    }

}
