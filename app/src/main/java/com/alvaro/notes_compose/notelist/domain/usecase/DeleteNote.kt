package com.alvaro.notes_compose.notelist.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNote(private val noteRepository: NoteRepository) {


    fun execute(
        note: Note,
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<Int>> = flow {


        try {
            emit( DataState.Data(data = noteRepository.deleteNote(note, forceExceptionForTesting)))
        } catch (e: Exception) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Toast(
                        message = ERROR_DELETING_NOTE
                    )
                )
            )
        }

    }

    companion object {
        const val ERROR_DELETING_NOTE = "Error deleting note from cache"
    }

}