package com.alvaro.notes_compose.notedetails.presentation

import androidx.lifecycle.*
import com.alvaro.core.domain.DataState
import com.alvaro.core.util.DispatcherProvider
import com.alvaro.core.util.Logger
import com.alvaro.notes_compose.R
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NotePriority
import com.alvaro.notes_compose.common.domain.NoteType
import com.alvaro.notes_compose.common.presentation.Action
import com.alvaro.notes_compose.common.presentation.BaseViewModel
import com.alvaro.notes_compose.common.presentation.Effect
import com.alvaro.notes_compose.common.presentation.ScreenState
import com.alvaro.notes_compose.common.utils.ResourceProvider
import com.alvaro.notes_compose.notedetails.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val hasNoteChangedUseCase: GetHasNoteEntityChangedUseCase,
    private val getNoteFromCache: GetNoteFromCache,
    private val getIsValidNote: GetIsValidNote,
    @Named("NoteDetailView") private val logger: Logger,
    private val dispatcherProvider: DispatcherProvider,
    private val resourceProvider: ResourceProvider,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<NoteDetailState, NoteDetailActions, NoteDetailEffects>() {

    companion object {
        private const val NOTE_ID_KEY = "noteId"
    }

    override fun createInitialScreenState(): NoteDetailState = NoteDetailState()

    init {
        savedStateHandle.get<String>(NOTE_ID_KEY)?.let { noteId ->
            if (noteId.isNotEmpty()) {
                viewModelScope.launch(dispatcherProvider.io()) {
                    getNoteById(noteId)
                }
            }
        }
    }

    override suspend fun handleActions(action: NoteDetailActions) {
        when (action) {
            is NoteDetailActions.GetNoteById -> {
                getNoteById(action.noteId)
            }
            is NoteDetailActions.NotePrioritySelected -> {
                setScreenState {
                    currentScreenState.copy(
                        data = data.copy(
                            note = data.note.copy(priority = action.priority)
                        )
                    )
                }
            }
            is NoteDetailActions.NoteTypeSelected -> {
                setScreenState {
                    currentScreenState.copy(
                        data = data.copy(
                            note = data.note.copy(noteType = action.type)
                        )
                    )
                }
            }
            is NoteDetailActions.OnUpdateTitle -> {
                setScreenState {
                    currentScreenState.copy(
                        data = data.copy(
                            note = data.note.copy(title = action.title)
                        )
                    )
                }
            }
            is NoteDetailActions.OnUpdateBody -> {
                setScreenState {
                    currentScreenState.copy(
                        data = data.copy(
                            note = data.note.copy(content = action.body)
                        )
                    )
                }
            }
            is NoteDetailActions.OnGoBackRequested -> {
                onBackButtonPressed()
            }
            is NoteDetailActions.OnGoBackConfirmed -> {
                setEffect { NoteDetailEffects.GoBack }
            }
            is NoteDetailActions.OnAddNoteButtonClicked -> {
                addNoteButtonClicked(currentScreenState.data.note)
            }
            is NoteDetailActions.OnDismissAlert -> {
                setScreenState { currentScreenState.copy(alertMessage = null) }
            }
        }
    }

    private suspend fun getNoteById(noteId: String) {
        setScreenState { currentScreenState.copy(isLoading = true) }

        getNoteByIdUseCase.execute(noteId).collect { dataState ->
            when (dataState) {
                is DataState.Data -> {
                    setScreenState {
                        currentScreenState.copy(
                            isLoading = false,
                            data = currentScreenState.data.copy(
                                note =  dataState.data
                            )
                        )
                    }
                }
                is DataState.ResponseError -> {
                    //logger.log((dataState.uiComponent as UIComponent.None).message)
                    setEffect {
                        NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.error_fetching_note))
                    }
                }
                else -> Unit
            }
        }
    }

    private suspend fun insertNote(note: Note) {
        setScreenState { currentScreenState.copy(isLoading = true) }

        insertNoteUseCase.execute(note).collect { dataState ->
            when (dataState) {
                is DataState.ResponseError -> {
                    //logger.log((dataState.uiComponent as UIComponent.None).message)
                    setEffect {
                        NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.error_saving_note))
                    }
                }
                is DataState.ResponseSuccess -> {
                    setScreenState { currentScreenState.copy(isLoading = false)  }
                    setEffect {
                        NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.note_saved_successfully))
                    }
                    setEffect { NoteDetailEffects.GoBack }
                }
                else -> Unit
            }
        }
    }

    private suspend fun updateNote(note: Note) {
        setScreenState { currentScreenState.copy(isLoading = true) }

        updateNoteUseCase.execute(note).collect { dataState ->
            when (dataState) {
                is DataState.ResponseError -> {
                    //logger.log((dataState.uiComponent as UIComponent.None).message)
                    setEffect {
                        NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.error_saving_note))
                    }
                }
                is DataState.ResponseSuccess -> {
                    setScreenState {  currentScreenState.copy(isLoading = false)   }
                    setEffect {
                        NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.note_saved_successfully))
                    }
                    setEffect { NoteDetailEffects.GoBack }
                }
                else -> Unit
            }
        }
    }

    private suspend fun addNoteButtonClicked(note: Note){
        if (getNoteFromCache(note.id) != null){
            updateNote(note)
        }else{
            if (getIsValidNote(note)){
                insertNote(note)
            }else{
                setEffect { NoteDetailEffects.ShowToast(resourceProvider.getString(R.string.note_not_valid)) }
            }
        }
    }

    private fun onBackButtonPressed(){
        if (hasNoteChangedUseCase(currentScreenState.data.note)){
            setScreenState {
                currentScreenState.copy(
                    alertMessage = AlertMessage(
                        title = resourceProvider.getString(R.string.leaving_room_title),
                        body = resourceProvider.getString(R.string.leaving_room_body)
                    )
                )
            }
        }else {
            setEffect { NoteDetailEffects.GoBack }
        }
    }

}

data class NoteDetailState(
    val data: Data = Data(),
    val isLoading: Boolean = false,
    val error : String? = null,
    val alertMessage: AlertMessage? = null
) : ScreenState

data class Data(val note: Note = Note.emptyNote() )

data class AlertMessage(
    val title: String = "",
    val body: String = ""
)

sealed class NoteDetailActions : Action {
    data class GetNoteById(val noteId: String) : NoteDetailActions()
    data class NotePrioritySelected(val priority: NotePriority) : NoteDetailActions()
    data class NoteTypeSelected(val type: NoteType) : NoteDetailActions()
    data class OnUpdateTitle(val title: String) : NoteDetailActions()
    data class OnUpdateBody(val body: String) : NoteDetailActions()
    object OnAddNoteButtonClicked : NoteDetailActions()
    object OnGoBackRequested : NoteDetailActions()
    object OnGoBackConfirmed : NoteDetailActions()
    object OnDismissAlert : NoteDetailActions()
}

sealed class NoteDetailEffects : Effect {
    object GoBack : NoteDetailEffects()
    data class ShowAlert(val title: String, val message: String) : NoteDetailEffects()
    data class ShowToast(val message: String) : NoteDetailEffects()
}
