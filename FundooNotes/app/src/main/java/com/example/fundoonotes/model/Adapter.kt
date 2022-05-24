package com.example.fundoonotes.model

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.FragmentNotes
import com.example.fundoonotes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Adapter(private var noteList: MutableList<Note>) : RecyclerView.Adapter<Adapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.notes_view_layout,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.MyViewHolder, position: Int) {
        val note: Note = noteList[position]
        holder.noteTitle.setText(note.title)
        holder.noteContent.setText(note.content)
        holder.noteID.setText(note.noteID)
        holder.mCardView.setCardBackgroundColor(getRandomColor())

        holder.view.setOnClickListener {

            val activity: AppCompatActivity = it.getContext() as AppCompatActivity

                note.title.let { it1 -> note.content?.let{ it2 -> note.noteID?.let { it3 ->
                    FragmentNotes(it1,
                        it2, it3)
                } } }
                    ?.let { it3 ->
                    activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentcontainer1,
                            it3
                        ).addToBackStack(null).commit()
                }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun filterList(filteredList: List<Note>){
        noteList = filteredList as MutableList<Note>
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noteTitle: TextView = itemView.findViewById(R.id.title)
        var noteContent: TextView = itemView.findViewById(R.id.content)
        var noteID: TextView = itemView.findViewById(R.id.noteID)
        var view = itemView
        var mCardView: CardView = itemView.findViewById(R.id.cardView)
    }

    private fun getRandomColor(): Int{
        val colorCode: ArrayList<Int> = ArrayList()
        colorCode.add(R.color.blue)
        colorCode.add(R.color.skyblue)
        colorCode.add(R.color.grey)
        colorCode.add(R.color.lightGreen)
        colorCode.add(R.color.lightPurple)
        colorCode.add(R.color.pink)
        colorCode.add(R.color.yellow)

        val randomColor: Random = Random()
        val number: Int = randomColor.nextInt(colorCode.size)
        return colorCode.get(number)
    }

}

