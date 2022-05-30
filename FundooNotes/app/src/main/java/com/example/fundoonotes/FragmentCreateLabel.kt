package com.example.fundoonotes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.model.LabelAdapter
import com.example.fundoonotes.view.ActivityDashboard


class FragmentCreateLabel: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_create_label, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton1)
        val createLabel: EditText = view.findViewById(R.id.createNewLabel)
        val checkBox: Button = view.findViewById(R.id.check)
        val recyclerView1: RecyclerView = view.findViewById(R.id.labelList)
        val label: String = createLabel.getText().toString()


        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        checkBox.setOnClickListener {
            val manager = LinearLayoutManager(this.context)
            recyclerView1.setLayoutManager(manager)
            recyclerView1.setHasFixedSize(true)
            val myLabelAdapter = LabelAdapter(label)
            recyclerView1.setAdapter(myLabelAdapter)
            myLabelAdapter.notifyDataSetChanged()
        }

        return view
    }
}