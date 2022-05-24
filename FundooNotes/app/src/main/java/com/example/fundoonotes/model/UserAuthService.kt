package com.example.fundoonotes.model

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.fundoonotes.ActivityDashboard
import com.example.fundoonotes.R
import com.example.fundoonotes.view.FragmentLogin
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.security.AccessController.getContext

class UserAuthService {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var firebaseUser: FirebaseUser
    lateinit var storageReference: StorageReference
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var userID: String

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

    }

    fun userLogin(email: String, password: String, listener: (AuthListener) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful()) {
                    listener(AuthListener(true, "User logged in successfully."))

                } else {
                    listener(AuthListener(false, "User failed to login"))
                }
            }
    }

    fun registerUser(
        email: String, password: String,fullName: String,
        listener: (AuthListener) -> Unit
    ) {

        var title: String
        var content: String
        var noteID: String
        var userID: String

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    userID = firebaseAuth.currentUser?.uid.toString()
                    listener(AuthListener(true, "User Registered successfully."))

                    val documentReference: DocumentReference = fstore.collection("users")
                        .document(userID)

                    val user: MutableMap<String, Any> = HashMap()
                    user["fullName"] = fullName
                    user["email"] = email

                    if (userID != null) {
                        documentReference.set(user).addOnSuccessListener {
                            (AuthListener(true, "User details recorded."))
                        }

                            .addOnFailureListener {
                                (AuthListener(false, "Failed to add details."))
                            }
                    }

                } else {
                    listener(AuthListener(false, "Registration failed."))
                }

            }
    }

    fun uploadImageToFirebase(imageUri: Uri, profileImage: CircleImageView) {

        storageReference = firebaseStorage.getReference()

        val fileRef: StorageReference = storageReference.child(
            "users/" + (firebaseAuth
                .getCurrentUser()?.getUid()) + "/profile.jpg")

        fileRef.putFile(imageUri).addOnSuccessListener {
            (AuthListener(true, "Image uploaded"))

            fileRef.getDownloadUrl().addOnSuccessListener {
                Picasso.get().load(imageUri).into(profileImage)
            }

        }.addOnFailureListener(OnFailureListener {
            (AuthListener(false, "Failed to upload image"))
        })

    }

    fun passwordReset(emailEntered:String){
        firebaseAuth.sendPasswordResetEmail(emailEntered).addOnCompleteListener { task ->

            if (task.isSuccessful()) {
                AuthListener(true, "Reset password link sent")
            } else {
                AuthListener(false, "Failed to send email")
            }
        }
    }

}