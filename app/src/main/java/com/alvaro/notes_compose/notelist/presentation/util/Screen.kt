package com.alvaro.notes_compose.notelist.presentation.util

sealed class Screen(val route: String) {

    object NoteListView: Screen(route = "notes_list_view")

    object NoteDetailsView: Screen(route ="note_details_view")

}