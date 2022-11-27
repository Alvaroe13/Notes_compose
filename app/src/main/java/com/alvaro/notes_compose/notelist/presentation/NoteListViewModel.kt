package com.alvaro.notes_compose.notelist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.domain.LoadingState
import com.alvaro.core.domain.UIComponent
import com.alvaro.core.util.DispatcherProvider
import com.alvaro.core.util.Logger
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.notelist.domain.usecase.DeleteNote
import com.alvaro.notes_compose.notelist.domain.usecase.GetNotes
import com.alvaro.notes_compose.notelist.domain.usecase.RemoveNoteFromCacheUseCase
import com.alvaro.notes_compose.notelist.domain.DeletionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotes: GetNotes,
    private val deleteNote: DeleteNote,
    private val removeNoteFromCacheUseCase: RemoveNoteFromCacheUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val eventManager: NoteListViewEventManager,
    @Named("NoteListView") private val logger: Logger,
) : ViewModel() {

    private val _state: MutableStateFlow<NoteListState> = MutableStateFlow(NoteListState())
    val state = _state.asStateFlow()

    private val _response: MutableSharedFlow<UIComponent> = MutableSharedFlow()
    val response = _response.asSharedFlow()

    init {
        triggerEvent(NoteListEvents.GetNotes)
    }


    fun triggerEvent(event: NoteListEvents) {

        if(eventManager.isEventActive(event)){
            return
        }
        eventManager.addEvent(event)

        _state.value = _state.value.copy(loadingState = LoadingState.Loading)

        when (event) {
            is NoteListEvents.GetNotes -> {
                //retrieveNoteList(event)
                getNotes()
            }
            is NoteListEvents.RemoveNoteFromCache -> {
                removeNoteFromCache(event)
            }
            is NoteListEvents.ConfirmDeletion -> {
                deleteNote(event)
            }
            is NoteListEvents.UndoDeletion -> {
                undoDeletion()
            }
        }
    }

    private fun retrieveNoteList(event: NoteListEvents) {
       viewModelScope.launch(dispatcherProvider.io()) {

            getNotes.execute().collect { dataState ->

                withContext(dispatcherProvider.main()){
                    when (dataState) {
                        is DataState.Data -> {
                           _state.value =  _state.value.copy(
                                noteList = dataState.data ?: emptyList(),
                                loadingState = LoadingState.Idle,
                                deletionState = DeletionState.Idle
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

        }.invokeOnCompletion {
            eventManager.removeEvent(event)
        }
    }

    private fun deleteNote(event : NoteListEvents.ConfirmDeletion){

        viewModelScope.launch(dispatcherProvider.io()) {

            deleteNote.execute(event.note).collect { dataState ->

                when (dataState) {
                    is DataState.Data -> {
                        triggerEvent(NoteListEvents.GetNotes)
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

        }.invokeOnCompletion {
            eventManager.removeEvent(event)
        }
    }

    private fun removeNoteFromCache(event: NoteListEvents.RemoveNoteFromCache) {

        viewModelScope.launch(dispatcherProvider.main()) {

            removeNoteFromCacheUseCase(event.note).also { dataState ->

                when (dataState) {
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            noteList = dataState.data ?: emptyList(),
                            loadingState = LoadingState.Idle,
                            deletionState = DeletionState.OnDeletion
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
        }.invokeOnCompletion {
            eventManager.removeEvent(event)
        }
    }

    private fun undoDeletion(){
        triggerEvent(NoteListEvents.GetNotes)
    }


    private fun getMockNotes() = mutableListOf<Note>().apply{
        add(Note(id = "1", title= "titulo 1", content = "Contenido 1", priority = 1, timeStamp = "time"))
        add(Note(id = "2", title= "titulo 2", content = "Contenido 2", priority = 1, timeStamp = "time"))
        add(Note(id = "3", title= "titulo 3", content = "Contenido 3", priority = 1, timeStamp = "time"))
    } .toList()

    fun getNotes(){
        viewModelScope.launch(dispatcherProvider.io()) {
            withContext(dispatcherProvider.main()){
                _state.value =  _state.value.copy(
                    noteList = getMockNotes(),
                    loadingState = LoadingState.Idle,
                    deletionState = DeletionState.Idle
                )
            }
        }
    }

}

sealed class NoteListEvents {
    object GetNotes : NoteListEvents()
    data class RemoveNoteFromCache(val note: Note) : NoteListEvents()
    data class ConfirmDeletion(val note: Note) : NoteListEvents()
    object UndoDeletion : NoteListEvents()
}

data class NoteListState(
    val noteList: List<Note> = emptyList(),
    val loadingState: LoadingState = LoadingState.Idle,
    val uiComponent: UIComponent = UIComponent.Empty,
    val deletionState: DeletionState = DeletionState.Idle
)