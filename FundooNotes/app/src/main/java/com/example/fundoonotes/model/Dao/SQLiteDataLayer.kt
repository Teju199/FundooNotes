package com.example.fundoonotes.model.Dao

import android.app.DownloadManager.COLUMN_TITLE
import android.content.ContentValues
import android.util.Log
import com.example.fundoonotes.model.Adapter
import com.example.fundoonotes.model.Note
import com.example.fundoonotes.model.NoteListener
import com.example.fundoonotes.view.*
import java.util.ArrayList

class SQLiteDataLayer: NotesDataLayer() {
    lateinit var allNotes: ArrayList<Note>
    lateinit var noteResults: ArrayList<Note>
    lateinit var myAdapter: Adapter

    override fun createNote(note1: Note, listener: ((NoteListener) -> Unit)?) {
        val cv: ContentValues = ContentValues()

        cv.put(COLUMN_TITLE, note1.title)
        cv.put(COLUMN_CONTENT, note1.content)
        cv.put(COLUMN_USERID, note1.userID)
        cv.put(COLUMN_ID, note1.noteID)
        cv.put(COLUMN_FLAG, note1.flag)
        cv.put(COLUMN_TIME, note1.time)
        cv.put(COLUMN_PRIORITY, note1.priority)
        cv.put(COLUMN_ARCHIVE, note1.isArchive)

        val insert = db.insert(NOTES_TABLE, null, cv)

        if (insert >= 0){
            Log.d(ContentValues.TAG, "Note added to SQLite")

        }else{
            Log.d(ContentValues.TAG, "Failed to add note.")
        }
    }

    override fun deleteNote(noteID: String, listener: ((NoteListener) -> Unit)?) {
        val query = "Select*FROM $NOTES_TABLE WHERE $COLUMN_ID = '$noteID'"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id = cursor.getString(3)
            db.delete(
                NOTES_TABLE, "$COLUMN_ID=?", arrayOf(
                    java.lang.String.valueOf(id)
                )
            )
            cursor.close()
        }
    }

    override fun getNote(listener: (ArrayList<Note>) -> Unit) {
    }

    override fun updateNote(
        editNoteTitle: String,
        editNoteContent: String,
        userID:String,
        noteID: String,
        listener: ((NoteListener) -> Unit)?
    ) {
        val args = ContentValues()
        args.put(COLUMN_TITTLE, editNoteTitle)
        args.put(COLUMN_CONTENT, editNoteContent)
        db.update(
            NOTES_TABLE, args, "$COLUMN_ID = ?", arrayOf(java.lang.String.valueOf(noteID))
        )
    }
}