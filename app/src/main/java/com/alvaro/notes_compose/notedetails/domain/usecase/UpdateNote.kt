package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateNote(private val noteRepository: NoteRepository) {


    fun execute(
        note: Note,
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<Int>> = flow {

        try {
            emit(DataState.Data(data = noteRepository.updateNote(note, forceExceptionForTesting)))
        } catch (e: Exception) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Toast(
                        message = ERROR_MSG
                    )
                )
            )
        }
    }


    companion object {
        const val ERROR_MSG = "Error updating note"
    }

}