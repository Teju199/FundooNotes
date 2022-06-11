package com.example.fundoonotes.view

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundoonotes.model.Dao.SQLiteDataLayer
import com.example.fundoonotes.model.Note

private val DATABASE_VERSION = 1
val NOTES_TABLE = "user_notes"
val COLUMN_TITTLE = "TITLE"
val COLUMN_CONTENT = "CONTENT"
val COLUMN_ID = "NOTE_ID"
val COLUMN_PRIORITY = "PRIORITY"
val COLUMN_USERID = "USER_ID"
val COLUMN_TIME = "TIME"
val COLUMN_FLAG = "FLAG"
val COLUMN_ARCHIVE = "ARCHIVED"

lateinit var db: SQLiteDatabase
lateinit var newNote: Note


class DataBaseHelper(context: Context?) :
    SQLiteOpenHelper(context, "newNotes3.db", null, DATABASE_VERSION) {

    var sqliteDataLayer = SQLiteDataLayer()


    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement =
            "CREATE TABLE $NOTES_TABLE($COLUMN_TITTLE TEXT,$COLUMN_CONTENT TEXT, $COLUMN_USERID TEXT, $COLUMN_ID TEXT PRIMARY KEY, $COLUMN_FLAG TEXT, $COLUMN_TIME TEXT, $COLUMN_PRIORITY TEXT, $COLUMN_ARCHIVE BOOL)"
        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addNote(note: Note) {

        db = this.writableDatabase
        sqliteDataLayer.createNote(note, null)
    }

    fun deleteNote(noteID: String) {

        db = this.writableDatabase
        sqliteDataLayer.deleteNote(noteID, null)
        db.close()
    }

    fun updateNote(updatedTitle: String, updatedContent: String, userID: String, noteID: String) {
        db = this.writableDatabase
        sqliteDataLayer.updateNote(updatedTitle, updatedContent, userID, noteID, null)
    }

    fun getAll(): ArrayList<Note> {

        val returnList: ArrayList<Note> = ArrayList<Note>()

        //To get data from database

        val queryString = "SELECT * FROM " + NOTES_TABLE
        val db: SQLiteDatabase = this.getReadableDatabase()
        val cursor: Cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {

            do {
                var title: String = cursor.getString(0)
                var content: String = cursor.getString(1)
                var userID: String = cursor.getString(2)
                var noteID: String = cursor.getString(3)
                var flag: String = cursor.getString(4)
                var time: String = cursor.getString(5)
                var priority: String = cursor.getString(6)
                var isArchive = cursor.getInt(7) == 1

                newNote = Note(title, content, userID, noteID, flag, time, priority)
                returnList.add(newNote)

            } while (cursor.moveToNext())

        }

        cursor.close()
        db.close()
        return returnList
    }

    /*fun syncNotes(){
        val queryString = "SELECT * FROM " + NOTES_TABLE
            val db: SQLiteDatabase = this.writableDatabase
            val result = db.rawQuery(queryString, null)

        if(COLUMN_FLAG.equals(false)) {

            if (result.moveToFirst()) {
                do {

                    var title: String = result.getString(0)
                    var content: String = result.getString(1)
                    var userID: String = result.getString(2)
                    var noteID: String = result.getString(3)
                    var flag: String = result.getString(4)
                    var time: String = result.getString(5)
                    var priority: String = result.getString(6)
                    var archived  = result.getInt(7) == 1

                    newNote = Note(title, content, userID, noteID, flag, time, priority)
                    val fragmentAddNotes: FragmentAddNotes = FragmentAddNotes()
                    fragmentAddNotes.saveNote(newNote)


                    val args = ContentValues()
                    args.put(COLUMN_TITTLE, "false")

                    db.update(
                        NOTES_TABLE, args, "$COLUMN_ID = ?", arrayOf(
                            java.lang.String.valueOf(true)
                        )
                    )

                } while (result.moveToNext())
            }
        }

    }*/

        /*var title : String = result.getString(0)
        var content : String = result.getString(1)
        var userID: String = result.getString(2)
        var noteID: String = result.getString(3)

        var note2: Note = Note(title, content, userID, noteID)*/
}
