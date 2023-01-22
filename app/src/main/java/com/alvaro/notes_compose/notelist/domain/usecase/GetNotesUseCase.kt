package com.alvaro.notes_compose.notelist.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNotesUseCase(private val noteRepository: NoteRepository){

    fun execute(
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<List<Note>>> = flow {

        try {
            emit(DataState.Data(data = noteRepository.getAllNotes(forceExceptionForTesting)))
        } catch (e: Exception) {
            emit(DataState.ResponseError())
        }
    }

}