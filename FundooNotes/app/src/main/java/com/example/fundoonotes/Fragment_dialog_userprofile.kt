package com.example.fundoonotes

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Fragment_dialog_userprofile : Fragment() {
    lateinit var firebaseAuth: FirebaseAuth
    private val PICK_IMAGE: Int = 1
    lateinit var uploadTask: UploadTask
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var fstore: FirebaseFirestore
    lateinit var documentReference: DocumentReference
    lateinit var profileImage: CircleImageView
    lateinit var imageUri: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_dialog_userprofile, container, false)

        var mFullName: TextView = view.findViewById(R.id.FullName)
        val memail: TextView = view.findViewById(R.id.email)
        val mlogout: Button = view.findViewById(R.id.logout)
        val changeProfile: Button = view.findViewById(R.id.changeProfileImage)
        val PICK_IMAGE: Int = 1
        profileImage = view?.findViewById(R.id.profile_image)!!
        val backButton: ImageView = view.findViewById(R.id.backButton)


        firebaseAuth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference()
        val userID: String? = firebaseAuth.getCurrentUser()?.getUid()
        val currentUser = firebaseAuth.getCurrentUser()
        val documentReference : String ?= firebaseAuth.currentUser?.uid
        val dialog: ProgressDialog = ProgressDialog(context)

        dialog.setTitle("Profile")

        fstore.collection("users").get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()

            if (it.isSuccessful()) {
                for (document in it.result!!) {
                    mFullName.setText(document.data.getValue("fullName").toString())
                    memail.setText(document.data.getValue("email").toString())
                }
            }
        }

        val profileRef: StorageReference = storageReference.child(
            "users/" + (firebaseAuth
                .getCurrentUser()?.getUid()) + "/profile.jpg"
        )

        profileRef.getDownloadUrl().addOnSuccessListener {
            Picasso.get().load(it).into(profileImage)
        }

        changeProfile.setOnClickListener(View.OnClickListener {
            val openGalleryIntent: Intent = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI
            )
            startActivityForResult(openGalleryIntent, PICK_IMAGE)
        })

        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }


        mlogout.setOnClickListener {
            firebaseAuth.signOut()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentLogin())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                imageUri = data?.getData()!!
                //profileImage?.setImageURI(imageUri)

                if (imageUri != null) {
                    uploadImageToFirebase(imageUri)
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val fileRef: StorageReference = storageReference.child(
            "users/" + (firebaseAuth
                .getCurrentUser()?.getUid()) + "/profile.jpg")

        fileRef.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show()

            fileRef.getDownloadUrl().addOnSuccessListener {
                Picasso.get().load(imageUri).into(profileImage)
            }

        }.addOnFailureListener(OnFailureListener {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show()
        })

    }
}