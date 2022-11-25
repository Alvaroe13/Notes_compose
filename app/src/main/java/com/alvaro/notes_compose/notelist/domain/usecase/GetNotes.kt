package com.alvaro.notes_compose.notelist.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNotes(private val noteRepository: NoteRepository){

    fun execute(
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<List<Note>>> = flow {

        try {
            val notes = noteRepository.getAllNotes(forceExceptionForTesting)
            emit(DataState.Data(data = notes))
        } catch (e: Exception) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error retrieving notes from cache",
                        message = e.localizedMessage ?: "Unknown error"
                    )
                )
            )
        }
    }

}