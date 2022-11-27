package com.alvaro.notes_compose.notedetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.LoadingState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.DispatcherProvider
import com.alvaro.core.util.Logger
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.notedetails.domain.SaveNoteType
import com.alvaro.notes_compose.notedetails.domain.usecase.GetNoteById
import com.alvaro.notes_compose.notedetails.domain.usecase.InsertNote
import com.alvaro.notes_compose.notedetails.domain.usecase.UpdateNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val insertNote: InsertNote,
    private val getNoteById: GetNoteById,
    private val updateNote: UpdateNote,
    @Named("NoteDetailView") private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: StateFlow<NoteDetailState> get() = _state
    private val _state: MutableStateFlow<NoteDetailState> = MutableStateFlow(NoteDetailState())

    val response: SharedFlow<UIComponent> get() = _response
    private val _response: MutableSharedFlow<UIComponent> = MutableSharedFlow()

    init {
        savedStateHandle.get<String>("noteId")?.let{ noteId ->
            if (noteId.isNotEmpty()){
                triggerEvent(NoteDetailsEvents.GetNoteById(noteId))
            }
        }
    }

    fun triggerEvent(event: NoteDetailsEvents) {

        _state.value = _state.value.copy(loadingState = LoadingState.Loading)

        when (event) {
            is NoteDetailsEvents.InsertNote -> {
                insertNote(event.note)
            }
            is NoteDetailsEvents.UpdateNote -> {
                updateNote(event.note)
            }
            is NoteDetailsEvents.GetNoteById -> {
                getNoteById(event.noteId)
            }
        }
    }

    private fun insertNote(note: Note) {

        viewModelScope.launch(dispatcherProvider.io()) {
            insertNote.execute(note).collect { dataState ->
                withContext(dispatcherProvider.main()) {

                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                loadingState = LoadingState.Idle,
                                saveNoteType = SaveNoteType.InsertNote
                            )
                        }
                        is DataState.Response -> {
                            _state.value = _state.value.copy(loadingState = LoadingState.Idle)
                            when (dataState.uiComponent) {
                                is UIComponent.None -> {
                                    logger.log((dataState.uiComponent as UIComponent.None).message)
                                }
                                else -> {
                                    _response.emit(dataState.uiComponent)
                                }
                            }
                        }
                    }

                }

            }
        }

    }

    private fun getNoteById(noteId: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            getNoteById.execute(noteId).collect { dataState ->
                withContext(dispatcherProvider.main()) {

                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                note = dataState.data,
                                loadingState = LoadingState.Idle
                            )
                        }
                        is DataState.Response -> {
                            when (dataState.uiComponent) {
                                is UIComponent.None -> {
                                    logger.log((dataState.uiComponent as UIComponent.None).message)
                                }
                                else -> {
                                    _response.emit(dataState.uiComponent)
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    private fun updateNote(note: Note) {
        viewModelScope.launch(dispatcherProvider.io()) {
            updateNote.execute(note).collect { dataState ->
                withContext(dispatcherProvider.main()) {

                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                loadingState = LoadingState.Idle,
                                saveNoteType = SaveNoteType.UpdateNote
                            )
                        }
                        is DataState.Response -> {
                            when (dataState.uiComponent) {
                                is UIComponent.None -> {
                                    logger.log((dataState.uiComponent as UIComponent.None).message)
                                }
                                else -> {
                                    _response.emit(dataState.uiComponent)
                                }
                            }
                        }
                    }

                }
            }
        }
    }


}

data class NoteDetailState(
    val note: Note? = null,
    val loadingState: LoadingState = LoadingState.Idle,
    val saveNoteType: SaveNoteType = SaveNoteType.None
)

sealed class NoteDetailsEvents{
    data class InsertNote(val note: Note): NoteDetailsEvents()
    data class UpdateNote(val note: Note): NoteDetailsEvents()
    data class GetNoteById(val noteId: String): NoteDetailsEvents()
}
