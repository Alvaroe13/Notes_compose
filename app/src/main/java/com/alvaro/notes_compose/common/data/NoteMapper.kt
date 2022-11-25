package com.alvaro.notes_compose.common.data

import com.alvaro.notes_compose.common.data.local.NoteEntity
import com.alvaro.notes_compose.common.domain.Note
import java.util.*

class NoteMapper {

    fun mapFrom(note: Note) : NoteEntity {
        return NoteEntity(
            id = note.id ?: UUID.randomUUID().toString(),
            title = note.title,
            content = note.content,
            priority = note.priority,
            timeStamp = note.timeStamp
        )
    }

    fun mapTo(noteEntity: NoteEntity): Note {
        return Note(
            id = noteEntity.id,
            title = noteEntity.title,
            content = noteEntity.content,
            priority = noteEntity.priority,
            timeStamp = noteEntity.timeStamp
        )
    }
}