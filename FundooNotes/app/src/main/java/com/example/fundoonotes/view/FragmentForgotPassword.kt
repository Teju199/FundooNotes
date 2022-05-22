package com.example.fundoonotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundoonotes.R
import com.example.fundoonotes.model.UserAuthService
import com.google.firebase.auth.FirebaseAuth


class FragmentForgotPassword : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        val firebaseAuth = FirebaseAuth.getInstance()

        val mbacktologin1: TextView = view.findViewById(R.id.backtologin1)
        val mforgotpassword: EditText = view.findViewById(R.id.forgotpassword)

        mbacktologin1.setOnClickListener(View.OnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer, FragmentLogin())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        })

        val mpasswordrecoverbtn: Button = view.findViewById<Button>(R.id.passwordrecoverbtn)

        mpasswordrecoverbtn.setOnClickListener(View.OnClickListener {
            val emailEntered = mforgotpassword.getText().toString().trim()
            if (emailEntered.isEmpty()) {
                Toast.makeText(
                    getContext(), "Enter valid email id",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val userAuthService: UserAuthService = UserAuthService()

                userAuthService.passwordReset(emailEntered)
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.replace(R.id.fragmentcontainer, FragmentLogin())
                    transaction.disallowAddToBackStack()
                    transaction.commit()
                }
            }
        })
        return view
    }
}
