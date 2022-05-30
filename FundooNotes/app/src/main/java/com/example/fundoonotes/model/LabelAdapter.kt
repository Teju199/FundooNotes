package com.example.fundoonotes.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R

class LabelAdapter(var label: String): RecyclerView.Adapter<LabelAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.label_view_layout,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.label.setText(label)
    }

    override fun getItemCount(): Int{
        return label.length
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val label: TextView = itemView.findViewById(R.id.labelName)

    }

}