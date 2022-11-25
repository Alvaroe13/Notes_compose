package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.TimeStampGenerator
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertNote(
    private val noteRepository: NoteRepository,
    private val timeStampGenerator: TimeStampGenerator
) {

    fun execute(
        note: Note,
        forceExceptionForTesting: Boolean = false
    ): Flow<DataState<Long>> = flow {

        if (!isValid(note)) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Toast(
                        message = ERROR_MSG_NO_VALID
                    )
                )
            )
            return@flow
        }

        note.timeStamp = timeStampGenerator.getDate() ?: "-"

        try {
            emit(DataState.Data(data = noteRepository.insertNote(note, forceExceptionForTesting)))
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Toast(message = SUCCESS_INSERTION_MSG)
                )
            )
        } catch (e: Exception) {
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Database Error",
                        message = ERROR_MSG_UNKNOWN
                    )
                )
            )
        }

    }

    private fun isValid(note: Note): Boolean {
        return note.title.isNotBlank() && note.content.isNotBlank()
    }

    companion object {
        const val ERROR_MSG_NO_VALID = "Title and content must be not blank"
        const val ERROR_MSG_UNKNOWN = "Error inserting note"
        const val SUCCESS_INSERTION_MSG = "Note saved successfully"
    }
}