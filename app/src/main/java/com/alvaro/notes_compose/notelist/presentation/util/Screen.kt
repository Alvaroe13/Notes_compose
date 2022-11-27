package com.alvaro.notes_compose.notelist.presentation.util

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {

    object NoteListView: Screen(route = "notes_list_view", arguments = emptyList())

    object NoteDetailsView: Screen(
        route ="note_details_view",
        arguments = listOf(
            navArgument("noteId"){
                type = NavType.StringType
            }
        )
    )

}