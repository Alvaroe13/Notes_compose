package com.alvaro.notes_compose.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S: ScreenState, A: Action, E: Effect>: ViewModel() {

    private val initialScreenState: S by lazy { createInitialScreenState() }
    protected abstract fun createInitialScreenState(): S

    protected val currentScreenState: S get() = screenState.value

    private val _screenState: MutableStateFlow<S> = MutableStateFlow(initialScreenState)
    val screenState = _screenState.asStateFlow()

    private val _actions: MutableSharedFlow<A> = MutableSharedFlow()
    protected val actions = _actions.asSharedFlow()

    private val _effect: Channel<E> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch{
            actions.collect {
                // to implement system preventing duplicate actions
                handleActions(it)
            }
        }
    }

    protected fun setScreenState(reduce: S.() -> S) {
        _screenState.value = currentScreenState.reduce()
    }

    fun setAction(action: A) = viewModelScope.launch { _actions.emit(action) }

    protected abstract suspend fun handleActions(action: A)

    protected fun setEffect(builder: () -> E) = viewModelScope.launch { _effect.send(builder()) }

}