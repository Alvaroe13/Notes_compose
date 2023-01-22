package com.alvaro.notes_compose.notelist.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.notelist.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NoteListView(
    viewModel: NoteListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val state by viewModel.screenState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { event ->
            when (event) {
                is NoteListEffects.OpenDetailScreen -> {
                    navController.navigate("${Screen.NoteDetailsView.route}?noteId=${event.noteId}")
                }
                is NoteListEffects.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    UIContent(state = state, action = viewModel::setAction)
}

@Composable
private fun UIContent(state: NoteListState, action: (NoteListActions) -> Unit) {
    when {
        state.isLoading -> LoadingContent()
        state.error != null -> ErrorContent(state)
        else -> ValidContent(state, action)
    }
}

@Composable
private fun ValidContent(state: NoteListState, action: (NoteListActions) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.noteList) { note ->
                NoteCard(note = note) { noteId ->
                    action(NoteListActions.NoteClicked(noteId))
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
                onClick = { action(NoteListActions.NoteClicked("")) },
                shape = RectangleShape,
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

@Composable
private fun ErrorContent(state: NoteListState) {
    // implement logic
}

@Composable
private fun LoadingContent() {
    // implement logic
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface,

                        )
                    Text(
                        text = note.timeStamp,
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSurface,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = note.priority.toString(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    NoteListView(navController = rememberNavController())
}