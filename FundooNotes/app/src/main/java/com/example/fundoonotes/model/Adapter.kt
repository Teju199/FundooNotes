package com.example.fundoonotes.model

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fundoonotes.view.FragmentNotes
import com.example.fundoonotes.R
import java.util.*

class Adapter(private var noteLists: ArrayList<Note>, val context: Context) : RecyclerView.Adapter<Adapter.MyViewHolder>(), Filterable{

    var noteFilterList = ArrayList<Note>()

    init{
        noteFilterList = this.noteLists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.notes_view_layout,parent,false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.MyViewHolder, position: Int) {
        val note: Note = noteFilterList[position]
        holder.noteTitle.setText(note.title)
        holder.noteContent.setText(note.content)
        holder.mDate.setText(note.time)

        holder.view.setOnClickListener {

            val activity: AppCompatActivity = it.getContext() as AppCompatActivity

            note.title.let { it1 ->
                note.content.let{ it2 ->
                    note.noteID.let { it3 ->
                        FragmentNotes(it1,
                            it2, it3)
                    }
                } }
                .let { it3 ->
                    activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentcontainer1,
                            it3
                        ).addToBackStack(null).commit()
                }
        }

        when(note.priority){

            "High" -> holder.itemView.setBackgroundColor(Color.parseColor("#85082F"))
            "medium" -> holder.itemView.setBackgroundColor(Color.parseColor("#FF03DAC5"))
            "low" -> holder.itemView.setBackgroundColor(Color.parseColor("#D4C13B"))
        }
    }


    override fun getItemCount(): Int {
        return noteFilterList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noteTitle: TextView = itemView.findViewById(R.id.title)
        var noteContent: TextView = itemView.findViewById(R.id.content)
        var view = itemView
        var mCardView: CardView = itemView.findViewById(R.id.cardView)
        var mDate: TextView = itemView.findViewById(R.id.date)
    }

    override fun getFilter(): Filter {
       return object: Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                if (charSequence.isEmpty()) {
                    noteFilterList = noteLists
                } else {
                    val resultList = ArrayList<Note>()
                    for (note in noteLists) {
                        if (note.title.lowercase(Locale.getDefault()).contains(charSequence.toString()) || note.content.lowercase(
                                Locale.getDefault()).contains(charSequence.toString())) {
                            resultList.add(note)
                        }
                        noteFilterList = resultList
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = noteFilterList
                return filterResults
            }

           override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               noteFilterList = results?.values as ArrayList<Note>
               notifyDataSetChanged()
           }
       }

    }


}

