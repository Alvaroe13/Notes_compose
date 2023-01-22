package com.alvaro.notes_compose.notelist.presentation

import androidx.lifecycle.viewModelScope
import com.alvaro.core.domain.DataState
import com.alvaro.core.util.DispatcherProvider
import com.alvaro.core.util.Logger
import com.alvaro.notes_compose.R
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.presentation.Action
import com.alvaro.notes_compose.common.presentation.BaseViewModel
import com.alvaro.notes_compose.common.presentation.Effect
import com.alvaro.notes_compose.common.presentation.ScreenState
import com.alvaro.notes_compose.common.utils.ResourceProvider
import com.alvaro.notes_compose.notelist.domain.DeletionState
import com.alvaro.notes_compose.notelist.domain.usecase.DeleteNoteUseCase
import com.alvaro.notes_compose.notelist.domain.usecase.GetNotesUseCase
import com.alvaro.notes_compose.notelist.domain.usecase.RemoveNoteFromCacheUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val removeNoteFromCacheUseCase: RemoveNoteFromCacheUseCase,
    private val eventManager: NoteListScreenEventManager,
    @Named("NoteListView") private val logger: Logger,
    private val resourceProvider: ResourceProvider,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<NoteListState, NoteListActions, NoteListEffects>() {

    init {
        viewModelScope.launch(dispatcherProvider.io()) {
            getNoteList()
        }
    }


//
//    fun triggerEvent(event: NoteListEvents) {
//
//        if(eventManager.isEventActive(event)){
//            return
//        }
//        eventManager.addEvent(event)
//
//        _state.value = _state.value.copy(loadingState = LoadingState.Loading)
//
//        when (event) {
//            is NoteListEvents.GetNotes -> {
//                retrieveNoteList(event)
//            }
//            is NoteListEvents.RemoveNoteFromCache -> {
//                removeNoteFromCache(event)
//            }
//            is NoteListEvents.ConfirmDeletion -> {
//                deleteNote(event)
//            }
//            is NoteListEvents.UndoDeletion -> {
//                undoDeletion()
//            }
//        }
//    }

//    private fun deleteNote(event : NoteListEvents.ConfirmDeletion){
//
//        viewModelScope.launch(dispatcherProvider.io()) {
//
//            deleteNote.execute(event.note).collect { dataState ->
//
//                when (dataState) {
//                    is DataState.Data -> {
//                        triggerEvent(NoteListEvents.GetNotes)
//                    }
//                    is DataState.Response -> {
//                        when (dataState.uiComponent) {
//                            is UIComponent.None -> {
//                                logger.log((dataState.uiComponent as UIComponent.None).message)
//                            }
//                            else -> {
//                                _response.emit(dataState.uiComponent)
//                            }
//                        }
//                    }
//                }
//
//            }
//
//        }.invokeOnCompletion {
//            eventManager.removeEvent(event)
//        }
//    }
//
//    private fun removeNoteFromCache(event: NoteListEvents.RemoveNoteFromCache) {
//
//        viewModelScope.launch(dispatcherProvider.main()) {
//
//            removeNoteFromCacheUseCase(event.note).also { dataState ->
//
//                when (dataState) {
//                    is DataState.Data -> {
//                        _state.value = _state.value.copy(
//                            noteList = dataState.data ?: emptyList(),
//                            loadingState = LoadingState.Idle,
//                            deletionState = DeletionState.OnDeletion
//                        )
//                    }
//                    is DataState.Response -> {
//                        when (dataState.uiComponent) {
//                            is UIComponent.None -> {
//                                logger.log((dataState.uiComponent as UIComponent.None).message)
//                            }
//                            else -> {
//                                _response.emit(dataState.uiComponent)
//                            }
//                        }
//                    }
//                }
//            }
//        }.invokeOnCompletion {
//            eventManager.removeEvent(event)
//        }
//    }
//
//    private fun undoDeletion(){
//        triggerEvent(NoteListEvents.GetNotes)
//    }

    override fun createInitialScreenState(): NoteListState = NoteListState()

    override suspend fun handleActions(action: NoteListActions) {
        when (action) {
            is NoteListActions.RemoveNoteFromCache -> Unit
            is NoteListActions.ConfirmDeletion -> Unit
            is NoteListActions.UndoDeletion -> Unit
            is NoteListActions.NoteClicked -> {
                setEffect { NoteListEffects.OpenDetailScreen(action.noteId) }
            }
            else -> Unit
        }
    }

    private suspend fun getNoteList() {
        setScreenState { currentScreenState.copy(isLoading = true) }

        getNotesUseCase.execute().collect { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    setScreenState {
                        currentScreenState.copy(
                            isLoading = false,
                            noteList = dataState.data
                        )
                    }
                }
                is DataState.ResponseError -> {
                    setScreenState {
                        currentScreenState.copy(isLoading = false)
                    }
                    setEffect { NoteListEffects.ShowToast(resourceProvider.getString(R.string.error_fetching_msg)) }
                }
                else -> Unit
            }
        }

    }

}

data class NoteListState(
    val noteList: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val deletionState: DeletionState = DeletionState.Idle
) : ScreenState

sealed class NoteListActions : Action {
    object GetNotes : NoteListActions()
    data class RemoveNoteFromCache(val note: Note) : NoteListActions()
    data class ConfirmDeletion(val note: Note) : NoteListActions()
    object UndoDeletion : NoteListActions()
    data class NoteClicked(val noteId: String) : NoteListActions()
}

sealed class NoteListEffects : Effect {
    data class ShowToast(val message: String) : NoteListEffects()
    data class OpenDetailScreen(val noteId: String) : NoteListEffects()
}