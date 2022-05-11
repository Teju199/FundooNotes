package com.example.fundoonotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(title: List<String>, content: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var title: List<String> = title
    var content : List<String> = content

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.notes_view_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.noteTitle.setText(title.get(position))
        holder.noteContent.setText(content.get(position))

        holder.view.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView = itemView.findViewById(R.id.title)
        var noteContent: TextView = itemView.findViewById(R.id.content)
        var view = itemView

    }


}