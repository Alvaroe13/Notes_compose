package com.alvaro.notes_compose.common.data

import com.alvaro.notes_compose.common.data.local.NoteEntity
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NotePriority
import com.alvaro.notes_compose.common.domain.NoteType
import java.util.*

class NoteMapper {

    fun mapFrom(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id ?: UUID.randomUUID().toString(),
            title = note.title,
            content = note.content,
            priority = note.priority.ordinal,
            timeStamp = note.timeStamp,
            noteType = note.noteType.name
        )
    }

    fun mapTo(noteEntity: NoteEntity): Note {
        return Note(
            id = noteEntity.id,
            title = noteEntity.title,
            content = noteEntity.content,
            priority = noteEntity.priority.mapNotePriority(),
            timeStamp = noteEntity.timeStamp,
            noteType = noteEntity.noteType.mapNoteType()
        )
    }

    private fun String.mapNoteType(): NoteType {
        return when(this){
            "GYM" -> NoteType.GYM
            "GROCERIES" -> NoteType.GROCERIES
            "GENERAL" -> NoteType.GENERAL
            else -> NoteType.GENERAL
        }
    }

    private fun Int.mapNotePriority(): NotePriority{
        return when(this){
            NotePriority.LOW.ordinal -> NotePriority.LOW
            NotePriority.MEDIUM.ordinal -> NotePriority.MEDIUM
            NotePriority.HIGH.ordinal -> NotePriority.HIGH
            else ->  NotePriority.LOW
        }
    }
}