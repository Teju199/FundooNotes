package com.example.fundoonotes.view

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.RegisterViewModel
import com.example.fundoonotes.viewmodel.RegisterViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.regex.Pattern
import kotlin.collections.HashMap

class FragmentRegister : Fragment() {

    lateinit var mSignUpEmail: EditText
    lateinit var mSignUpPassword: EditText
    lateinit var mConfirmPassword: EditText
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var fragment: Fragment
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var email: String
    lateinit var documentReference: DocumentReference
    lateinit var storageReference: StorageReference
    lateinit var fstorage: FirebaseStorage
    lateinit var mFullName: EditText
    lateinit var userAuthService: UserAuthService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        mSignUpEmail = view.findViewById(R.id.signupemail)
        val mSignUp: RelativeLayout = view.findViewById(R.id.register)
        val mBackToLogin: TextView = view.findViewById(R.id.backtologin)
        mSignUpPassword = view.findViewById(R.id.signuppassword)
        mConfirmPassword = view.findViewById(R.id.confirmpassword)
        mFullName = view.findViewById(R.id.fullName)

        firebaseAuth = FirebaseAuth.getInstance()
        userAuthService = UserAuthService()

        //implementing action for back to login button on sign up page
        mBackToLogin.setOnClickListener(View.OnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer, FragmentLogin())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        })

        mSignUp.setOnClickListener(View.OnClickListener {
            val name: String = mFullName.getText().toString().trim()
            val email: String = mSignUpEmail.getText().toString().trim()
            val password: String = mSignUpPassword.getText().toString().trim()
            val confirmedPassword: String = mConfirmPassword.getText().toString().trim()

            signupProcess(name, email, password, confirmedPassword)
        })

        return view
    }

    fun isValidPassword(password: String?): Boolean {
        val passwordPattern = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun signupProcess(
        name: String,
        email: String,
        password: String,
        confirmedPassword: String
    ) {

        val RegisterViewModel: RegisterViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService()))
            .get(RegisterViewModel::class.java)

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                getContext(), "All fields are required",
                Toast.LENGTH_SHORT
            ).show()
        } else if (password.length < 7) {
            mSignUpPassword.setError("Password length is small")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mSignUpEmail.setError("Invalid email.")
        } else if (isValidPassword(password) == false) {
            mSignUpPassword.setError("Invalid password.")
        } else if (isValidEmail(email) == false) {
            mSignUpEmail.setError("Invalid email.")
        } else if (password.equals(confirmedPassword) == false) {
            Toast.makeText(
                getContext(), "Password do not match",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            RegisterViewModel.registerUser(email, password, name)
            RegisterViewModel.registerStatus.observe(viewLifecycleOwner, Observer {

                if (it.status) {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()

                    val firebaseUser: FirebaseUser? = firebaseAuth.getCurrentUser()

                    if (firebaseUser != null) {
                        firebaseUser.sendEmailVerification().addOnCompleteListener {
                            Toast.makeText(
                                getContext(), "Verification email sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            firebaseAuth?.signOut()

                        }
                    }

                } else {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
