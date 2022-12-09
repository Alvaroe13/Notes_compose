package com.alvaro.notes_compose.common.data

import com.alvaro.notes_compose.common.data.local.NoteEntity
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteType
import java.util.*

class NoteMapper {

    fun mapFrom(note: Note): NoteEntity {
        return NoteEntity(
            id = note.id ?: UUID.randomUUID().toString(),
            title = note.title,
            content = note.content,
            priority = note.priority,
            timeStamp = note.timeStamp,
            noteType = note.noteType.name
        )
    }

    fun mapTo(noteEntity: NoteEntity): Note {
        return Note(
            id = noteEntity.id,
            title = noteEntity.title,
            content = noteEntity.content,
            priority = noteEntity.priority,
            timeStamp = noteEntity.timeStamp,
            noteType = mapNoteType(noteEntity.noteType)
        )
    }

    private fun mapNoteType(noteType: String): NoteType {
        return when(noteType){
            "GYM" -> NoteType.GYM
            "GROCERIES" -> NoteType.GROCERIES
            "GENERAL" -> NoteType.GENERAL
            else -> NoteType.GENERAL
        }
    }
}