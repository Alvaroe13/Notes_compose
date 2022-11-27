package com.alvaro.notes_compose.notelist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.notelist.presentation.util.Screen


@Composable
fun NoteListView(
    viewModel: NoteListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state = viewModel.state.collectAsState().value
    val notes = state.noteList

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notes) { note ->
                NoteCard(note = note) { noteId ->
                    //navigateToDetailView(it)
                    navController.navigate("${Screen.NoteDetailsView.route}?noteId=$noteId")
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier
                    .width(55.dp)
                    .height(55.dp),
                onClick = {
                    //navigateToDetailView()
                    navController.navigate(Screen.NoteDetailsView.route)
                },
                shape = RectangleShape,
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

@Composable
fun NoteCard(note: Note, onSelectedNote: (noteId: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable {
                onSelectedNote(
                    note.id ?: throw NullPointerException("Note id is null and is musnt")
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,

                )
            Text(
                text = note.timeStamp,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = note.priority.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    NoteListView(navController = rememberNavController())
}