package com.example.fundoonotes.model.Dao

import android.database.sqlite.SQLiteDatabase
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.view.DataBaseHelper
import java.util.ArrayList

abstract class NotesDataLayer {

    //abstract val writableDatabase: SQLiteDatabase

    abstract fun createNote(note1: Note, listener: ((NoteListener) -> Unit)?)

    abstract fun deleteNote(noteID: String, listener: ((NoteListener) -> Unit)?)

    abstract fun getNote(listener: (ArrayList<Note>) -> (Unit))

    abstract fun updateNote(editNoteTitle: String, editNoteContent: String,userID: String, noteID: String, listener: ((NoteListener) -> Unit)?)

}