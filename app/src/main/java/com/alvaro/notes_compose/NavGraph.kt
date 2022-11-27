package com.alvaro.notes_compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.alvaro.notes_compose.notedetails.presentation.NoteDetailsView
import com.alvaro.notes_compose.notelist.presentation.NoteListView
import com.alvaro.notes_compose.notelist.presentation.util.Screen

@Composable
fun SetUpNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.NoteListView.route,
    ) {

        composable(route = Screen.NoteListView.route) {
            NoteListView(navController = navController)
        }

        composable(
            route = Screen.NoteDetailsView.route + "?noteId={noteId}",
            arguments = listOf(
                navArgument(name = "noteId"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            NoteDetailsView()
        }

    }

}