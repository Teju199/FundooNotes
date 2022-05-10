package com.example.fundoonotes

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class FragmentRegister : Fragment() {

    lateinit var mSignUpEmail: EditText
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var fragment: Fragment
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var email: String
    lateinit var documentReference: DocumentReference
    lateinit var storageReference: StorageReference
    lateinit var fstorage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_register, container, false)

        mSignUpEmail = view.findViewById(R.id.signupemail)
        val mSignUpPassword: EditText = view.findViewById(R.id.signuppassword)
        val mConfirmPassword: EditText = view.findViewById(R.id.confirmpassword)
        val mSignUp: RelativeLayout = view.findViewById(R.id.register)
        val mBackToLogin: TextView = view.findViewById(R.id.backtologin)

        firebaseAuth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()


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
            email = mSignUpEmail.getText().toString().trim()
            val password: String = mSignUpPassword.getText().toString().trim()
            val confirmedPassword: String = mConfirmPassword.getText().toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    getContext(), "All fields are required",
                    Toast.LENGTH_SHORT).show()
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
                    Toast.LENGTH_SHORT).show()
            } else {
                activity?.let { it1 ->
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener{
                            if (it.isSuccessful()) {
                                Toast.makeText(
                                    getContext(), "Registration successful",
                                    Toast.LENGTH_SHORT).show()
                                sendEmailVerification(email)
                                val mName : EditText? = view?.findViewById(R.id.fullName)
                                val fullName = mName?.getText().toString()

                                saveFireStore(fullName, email)

                            } else {
                                Toast.makeText(
                                    getContext(), "Registration failed",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }
                    }
                }
        })
        return view
    }

    private fun saveFireStore(fullName: String, email: String) {

        val user: MutableMap<String, Any> = HashMap()
        user["fullName"] = fullName
        user["email"] = email

        fstore.collection("users").add(user).addOnSuccessListener {
            Toast.makeText(context, "Record added successfully", Toast.LENGTH_LONG).show()

        }
            .addOnFailureListener{
                Toast.makeText(context, "Failed to add record", Toast.LENGTH_LONG).show()
            }
    }

    private fun isValidPassword(password: String?): Boolean{
        val passwordPattern = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun isValidEmail(email: String): Boolean{
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun sendEmailVerification(email: String) {
        val firebaseUser: FirebaseUser? = firebaseAuth?.getCurrentUser()
        val mName : EditText? = view?.findViewById(R.id.fullName)
        val fullName = mName?.getText().toString()

        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener{
                Toast.makeText(getContext(), "Verification email sent",
                    Toast.LENGTH_SHORT).show()
                firebaseAuth?.signOut()

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.replace(R.id.fragmentcontainer, FragmentLogin())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
            }
        }

        else{
            Toast.makeText(getContext(), "Failed to send verification email",
                Toast.LENGTH_SHORT).show()
        }
    }
}