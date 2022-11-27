package com.alvaro.notes_compose.notelist.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alvaro.notes_compose.common.domain.Note


@Composable
fun NoteListView(
    viewModel: NoteListViewModel = hiltViewModel(),
    navigateToDetailView: (noteId: String) -> Unit
) {
    val state = viewModel.state.collectAsState().value
    val notes = state.noteList

    if(notes.isNotEmpty()){
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notes){ note ->
                NoteCard(note = note) {
                    navigateToDetailView(it)
                }
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
            .clickable { onSelectedNote(note.id ?: throw NullPointerException("Note id is null and is musnt")) }
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