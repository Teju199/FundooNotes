package com.example.fundoonotes.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fundoonotes.R
import com.example.fundoonotes.model.Label


class FragmentCreateLabel: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_create_label, container, false)

        val backButton: ImageView = view.findViewById(R.id.backButton1)
        val createLabel: EditText = view.findViewById(R.id.createNewLabel)
        val checkBox: Button = view.findViewById(R.id.check)
        val list: ListView = view.findViewById(R.id.labelList)
        val label: String = createLabel.getText().toString()

        var labels: MutableList<Label> = mutableListOf()
        for(label in labels){
            labels.add(label)
        }

        backButton.setOnClickListener {
            val intent: Intent = Intent(getActivity(), ActivityDashboard::class.java)
            startActivity(intent)
        }

        checkBox.setOnClickListener {
            Toast.makeText(context, "clicked add", Toast.LENGTH_LONG).show()
            val labelArrayAdapter: ArrayAdapter<Label> = ArrayAdapter<Label>(requireContext(), android.R.layout.simple_list_item_1, labels)
            list.setAdapter(labelArrayAdapter)

        }
        return view
    }
}