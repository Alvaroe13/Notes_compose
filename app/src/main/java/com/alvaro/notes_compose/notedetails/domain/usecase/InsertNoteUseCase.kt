package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.core.util.TimeStampGenerator
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNoteUseCase(
    private val noteRepository: NoteRepository,
    private val timeStampGenerator: TimeStampGenerator
) {

    fun execute(
        note: Note,
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<Unit>> = flow {

        note.timeStamp = timeStampGenerator.getDate() ?: "-"

        try {
            noteRepository.insertNote(note, forceExceptionForTesting)
            emit(DataState.ResponseSuccess())
        } catch (e: Exception) {
            emit( DataState.ResponseError())
        }

    }

}