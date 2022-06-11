package com.example.fundoonotes.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.R
import com.example.fundoonotes.model.UserAuthService
import com.example.fundoonotes.viewmodel.LoginViewModel
import com.example.fundoonotes.viewmodel.LoginViewModelFactory
import com.example.fundoonotes.viewmodel.SharedViewModel
import com.example.fundoonotes.viewmodel.SharedViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin : Fragment() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    val RC_SIGN_IN: Int = 100
    private lateinit var loginViewModel: LoginViewModel
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        val mLoginEmail: EditText = view.findViewById(R.id.loginemail)
        val mLoginPassword: EditText = view.findViewById(R.id.loginpassword)
        val mLoginBtn: RelativeLayout = view.findViewById(R.id.login)
        val mForgotPassword1: TextView = view.findViewById(R.id.forgotpassword1)
        val mGoToSignUp: RelativeLayout = view.findViewById(R.id.gotosignup)
        val mGoogleBtn: SignInButton = view.findViewById(R.id.googlebtn)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService()))
            .get(LoginViewModel::class.java)

        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory(UserAuthService())
        )[SharedViewModel::class.java]

        mGoogleBtn.setSize(SignInButton.SIZE_STANDARD)


        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().build()
        gsc = GoogleSignIn.getClient(requireContext(), getGSO())


        firebaseAuth = FirebaseAuth.getInstance()

        mGoToSignUp.setOnClickListener(View.OnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer, FragmentRegister())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        })

        mForgotPassword1.setOnClickListener(View.OnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer, FragmentForgotPassword())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        })

        mGoogleBtn.setOnClickListener(View.OnClickListener {
            signIn()
        })

        mLoginBtn.setOnClickListener(View.OnClickListener {

            val email: String = mLoginEmail.getText().toString().trim()
            val password: String = mLoginPassword.getText().toString().trim()
            var returnSecureToken: Boolean = true

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    getContext(), "All fields are required",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                loginViewModel.userLogin(email, password)
                loginViewModel.loginStatus.observe(viewLifecycleOwner, Observer {

                    if (it.status) {
                        checkEmailVerification()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        val intent: Intent = Intent(activity, ActivityDashboard::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        sharedViewModel.setGotoLoginPageStatus(true)
                    }
                })
            }
        })
        return view
    }

    private fun getGSO(): GoogleSignInOptions {

        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(("270965940065-uh19qjopcooor9uuaikts3kdl5sel4cv.apps.googleusercontent.com"))
            .requestEmail()
            .build()
    }

    private fun signIn() {
        val signInIntent: Intent? = gsc?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)

        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)

        } catch (e: ApiException) {
            Log.d("failed code", e.toString())

        }
    }


    private fun checkEmailVerification() {

        var firebaseUser = FirebaseAuth.getInstance().getCurrentUser()

        if (firebaseUser != null) {
            if (firebaseUser.isEmailVerified) {
                Toast.makeText(
                    getContext(), "Logged In",
                    Toast.LENGTH_SHORT
                ).show()

                activity?.getFragmentManager()?.popBackStack()
                val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
                startActivity(intent)
            }

        } else {
            Toast.makeText(
                getContext(), "Email verification is pending",
                Toast.LENGTH_SHORT
            ).show()
            firebaseAuth?.signOut()
        }
    }

}
