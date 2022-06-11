package com.example.fundoonotes.model

class NoteModel() {

    var title: String = " "
    var content: String = " "
    var noteID: String = " "
    var time: String = " "
    var priority: String = " "
    var userID: String = " "

    constructor(noteID: String, title: String, content: String, priority: String):this(){
        this.noteID = noteID
        this.title = title
        this.content = content
        this.priority = priority
    }

    override fun toString(): String {
        return "NoteModel(title='$title', content='$content', noteID='$noteID', time='$time', priority='$priority')"
    }

}