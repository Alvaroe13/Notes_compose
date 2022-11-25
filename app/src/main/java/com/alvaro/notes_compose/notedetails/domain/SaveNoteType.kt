package com.alvaro.notes_compose.notedetails.domain

sealed interface SaveNoteType {
    object InsertNote: SaveNoteType
    object UpdateNote: SaveNoteType
    object None: SaveNoteType
}