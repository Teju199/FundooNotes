package com.example.fundoonotes.view

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.fundoonotes.R

class FragmentLabel: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_label, container, false)

        val redToggleButton: ImageView = view.findViewById(R.id.redToggleBtn)
        val blueToggleButton: ImageView = view.findViewById(R.id.blueToggleBtn)
        val yellowToggleButton: ImageView = view.findViewById(R.id.yellowToggleBtn)
        val backButton: ImageView = view.findViewById(R.id.backbtn)

        redToggleButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentHighPriority())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        blueToggleButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentMediumPriority())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }

        }

        yellowToggleButton.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.fragmentcontainer1, FragmentLowPriority())
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }

        backButton.setOnClickListener {
            val intent: android.content.Intent =
                android.content.Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }
        return view
    }

}