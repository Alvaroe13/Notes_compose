package com.alvaro.notes_compose.notelist.domain

sealed interface DeletionState {
    object Idle: DeletionState
    object OnDeletion: DeletionState
}