package com.alvaro.notes_compose.common.data.local

import androidx.room.*

@Dao
interface NotesDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity) : Long

    @Update
    suspend fun updateNote(note: NoteEntity) : Int

    @Delete
    suspend fun deleteNote(note: NoteEntity) : Int

    @Query("SELECT * FROM noteDb ORDER BY priority DESC, timeStamp DESC")
    fun getNotesByPriorityDesc() : List<NoteEntity>

    @Query("SELECT * FROM noteDb WHERE id = :noteId")
    fun getNoteById(noteId : String) : NoteEntity
}