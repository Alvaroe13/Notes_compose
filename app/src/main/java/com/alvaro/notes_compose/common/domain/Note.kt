package com.alvaro.notes_compose.common.domain


data class Note(
    var id: String? = null,
    var title: String,
    var content: String,
    var priority: NotePriority,
    var timeStamp: String,
    var noteType: NoteType
) {

    companion object Factory {
        fun emptyNote(): Note {
            return Note(null, "", "", NotePriority.LOW, "", NoteType.GENERAL)
        }
    }
}

enum class NoteType(name: String){
    GENERAL(name = "GENERAL"),
    GROCERIES(name = "GROCERIES"),
    GYM(name = "GYM"),
}

enum class NotePriority{ LOW, MEDIUM, HIGH }