package com.alvaro.notes_compose.notelist.presentation.util

sealed class Screen(val route: String) {
    object NoteListView: Screen("notes_list_view")
    object NoteDetailsView: Screen("note_details_view")
}