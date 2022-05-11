package com.example.fundoonotes

import android.content.Intent
import android.net.Uri
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class FragmentLogin : Fragment() {

    var firebaseAuth: FirebaseAuth? = null
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null
    val RC_SIGN_IN: Int = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        val mLoginEmail: EditText = view.findViewById(R.id.loginemail)
        val mLoginPassword: EditText = view.findViewById(R.id.loginpassword)
        val mLoginBtn: RelativeLayout = view.findViewById(R.id.login)
        val mForgotPassword1: TextView = view.findViewById(R.id.forgotpassword1)
        val mGoToSignUp: RelativeLayout = view.findViewById(R.id.gotosignup)
        val mGoogleBtn: SignInButton = view.findViewById(R.id.googlebtn)

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

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    getContext(), "All fields are required",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                firebaseAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful()) {
                            checkEmailVerification()
                        } else {
                            Toast.makeText(
                                getContext(), "Account does not exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })
        return view
    }

    private fun getGSO(): GoogleSignInOptions {

        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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
            //val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            val acct: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())

            if (acct != null) {
                val personName: String? = acct.getDisplayName()
                val personGivenName: String? = acct.getGivenName()
                val personFamilyName: String? = acct.getFamilyName()
                val personEmail: String? = acct.getEmail()
                val personId: String? = acct.getId()
                val personPhoto: Uri? = acct.getPhotoUrl()
            }

            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)

        } catch (e: ApiException) {
            Log.d("failed code", e.toString())

        }
    }

    private fun checkEmailVerification() {

        var firebaseUser = FirebaseAuth.getInstance().getCurrentUser()

        if (firebaseUser != null) {
            if (firebaseUser?.isEmailVerified == true) {
                Toast.makeText(
                    getContext(), "Logged In",
                    Toast.LENGTH_SHORT
                ).show()

                getActivity()?.getFragmentManager()?.popBackStack()
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
