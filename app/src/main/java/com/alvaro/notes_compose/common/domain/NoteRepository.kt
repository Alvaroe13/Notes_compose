package com.alvaro.notes_compose.common.domain

interface NoteRepository {

    suspend fun insertNote(note: Note, forceExceptionForTesting: Boolean = false): Long
    suspend fun updateNote(note: Note, forceExceptionForTesting: Boolean = false): Int
    suspend fun deleteNote(note: Note, forceExceptionForTesting: Boolean = false): Int
    suspend fun getAllNotes(forceExceptionForTesting: Boolean = false): List<Note>
    suspend fun getNoteById(noteId: String, forceExceptionForTesting: Boolean = false): Note
    fun getCacheNotes(forceExceptionForTesting: Boolean = false): List<Note>

}