package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNoteUseCase(private val noteRepository: NoteRepository) {

    fun execute(
        note: Note,
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<Unit>> = flow {

        try {
            noteRepository.updateNote(note, forceExceptionForTesting)
            emit(DataState.ResponseSuccess())
        } catch (e: Exception) {
            emit(DataState.ResponseError())
        }
    }

}