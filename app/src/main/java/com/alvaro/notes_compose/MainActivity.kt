package com.alvaro.notes_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alvaro.notes_compose.notedetails.presentation.NoteDetailsView
import com.alvaro.notes_compose.notelist.presentation.NoteListView
import com.alvaro.notes_compose.notelist.presentation.util.Screen
import com.alvaro.notes_compose.ui.theme.Notes_ComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Notes_ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NoteListView.route
                    ) {

                        composable(route = Screen.NoteListView.route) {
                            NoteListView { noteId ->
                                navController.navigate("${Screen.NoteDetailsView.route}/$noteId")
                            }
                        }

                        composable(route = Screen.NoteDetailsView.route + "/{noteId}"){
                            NoteDetailsView()
                        }

                    }

                }
            }
        }
    }

    /*@Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Notes_ComposeTheme {
            Greeting("Android")
        }
    }*/

}