package com.alvaro.notes_compose.common.data

import com.alvaro.notes_compose.common.data.local.NotesDatabase
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository

class NoteRepositoryImpl (
    private val database: NotesDatabase,
    private val noteMapper: NoteMapper
) : NoteRepository {

    private val notesCached = mutableListOf<Note>()

    override suspend fun insertNote(note : Note, forceExceptionForTesting: Boolean) : Long {
        return database.getDao().insertNote(noteMapper.mapFrom(note))
    }

    override suspend fun updateNote(note: Note, forceExceptionForTesting: Boolean): Int {
        return database.getDao().updateNote(noteMapper.mapFrom(note))
    }

    override suspend fun deleteNote(note: Note, forceExceptionForTesting: Boolean) : Int {
        return database.getDao().deleteNote(noteMapper.mapFrom(note))
    }

    override suspend fun getAllNotes(forceExceptionForTesting: Boolean): List<Note> {
        return database.getDao().getNotesByPriorityDesc().map { noteMapper.mapTo(it) }.also { updateNotesCached(it) }
    }

    override suspend fun getNoteById(noteId: String, forceExceptionForTesting: Boolean): Note {
        return noteMapper.mapTo( database.getDao().getNoteById(noteId) )
    }

    override fun getCacheNotes(forceExceptionForTesting: Boolean): List<Note> {
        return notesCached
    }

    private fun updateNotesCached(notes: List<Note>){
        notesCached.clear()
        notesCached.addAll(notes)
    }

}