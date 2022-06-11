package com.example.fundoonotes.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.R
import java.util.ArrayList

class LabelAdapter(var noteLists: ArrayList<Note>): RecyclerView.Adapter<LabelAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.notes_view_layout,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note: Note = noteLists[position]
        holder.noteTitle.setText(note.title)
        holder.noteContent.setText(note.content)
        holder.mDate.setText(note.time)
    }

    override fun getItemCount(): Int{
        return noteLists.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noteTitle: TextView = itemView.findViewById(R.id.title)
        var noteContent: TextView = itemView.findViewById(R.id.content)
        var mDate: TextView = itemView.findViewById(R.id.date)

    }

}