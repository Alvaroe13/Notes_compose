package com.alvaro.notes_compose.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alvaro.notes_compose.common.domain.NoteType

@Entity(tableName = NotesDatabase.DATABASE_NAME)
class NoteEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var title: String = "",
    var content: String = "",
    var priority: Int = 0,
    var timeStamp: String = "",
    var noteType: String = NoteType.GENERAL.name
)