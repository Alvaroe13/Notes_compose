package com.alvaro.notes_compose.notedetails.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.R
import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NotePriority
import com.alvaro.notes_compose.common.domain.NoteType
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val state = viewModel.state.collectAsState().value

    var note: Note by remember { mutableStateOf( Note.emptyNote() ) }

    note = state.note ?: note


    var showAlertDialog by rememberSaveable { mutableStateOf(false) }
    if (showAlertDialog) {
        Dialog(
            onDismiss = { showAlertDialog = false },
            onConfirm = { navController.navigateUp() }
        )
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is UIComponent.Dialog -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UIComponent.Toast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                actions = {
                    SpinnerNoteActions(
                        notePrioritySelected = { note = note.copy(priority = it) },
                        noteTypeSelected = { note = note.copy(noteType = it) }
                    )
                },
                goBackListener = {
                    if (shouldShowDialog(note, state)){
                        showAlertDialog = true
                    }else{
                        navController.navigateUp()
                    }
                }
            )
        },
        content = {
            NoteDetailContent(
                title = note.title,
                body = note.content,
                onTitleChanged = { note = note.copy( title = it) },
                onBodyChanged = { note = note.copy( content = it) }
            )
        },
        floatingActionButton = {
            FAB {
                if (state.note == null){
                    insertNote(viewModel, note)
                }else{
                    updateNote(viewModel, note)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    )
}

private fun shouldShowDialog(note: Note, state: NoteDetailState): Boolean {
    if (state.note != null ){
        if(note.title != state.note.title ||
            note.content != state.note.content ||
            note.noteType != state.note.noteType ||
            note.priority != state.note.priority ) {
            return true
        }
    }else{
        if(note.title.isNotEmpty() ||
            note.content.isNotEmpty() ||
            note.noteType != NoteType.GENERAL ||
            note.priority != NotePriority.LOW ){
            return true
        }
    }
    return false
}


private fun insertNote(viewModel: NoteDetailViewModel, note: Note){
    viewModel.triggerEvent(NoteDetailsEvents.InsertNote(note))
}

private fun updateNote(viewModel: NoteDetailViewModel, note: Note){
    viewModel.triggerEvent(NoteDetailsEvents.UpdateNote(note))
}

@Composable
fun SpinnerNoteActions(
    notePrioritySelected: (type: NotePriority) -> Unit,
    noteTypeSelected: (type: NoteType) -> Unit
) {

    Box {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 15.dp)
        ) {
            SpinnerNoteType { noteTypeSelected(it) }
            SpinnerNotePriority { notePrioritySelected(it) }
        }
    }
}

@Composable
fun SpinnerNoteType(noteTypeSelected: (type: NoteType) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .align(Alignment.TopEnd)
                .padding(end = 15.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_pen_24), contentDescription = null)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                NoteType.values().forEach { noteType ->
                    DropdownMenuItem(
                        onClick = {
                            noteTypeSelected(noteType)
                            expanded = false
                        }
                    ) {
                        Text(text = noteType.name)
                    }
                }
            }
        }
    }
}

@Composable
fun SpinnerNotePriority(notePrioritySelected: (type: NotePriority) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .align(Alignment.TopEnd)
                .padding(end = 15.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_filter_24), contentDescription = null)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                NotePriority.values().forEach { notePriority ->
                    DropdownMenuItem(
                        onClick = {
                            notePrioritySelected(notePriority)
                            expanded = false
                        }
                    ) {
                        Text(text = notePriority.name)
                    }
                }
            }
        }
    }
}

@Composable
fun Toolbar(
    actions: @Composable () -> Unit,
    goBackListener: () -> Unit
) {

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name), color = Color.White) },
        backgroundColor = colorResource(id = R.color.teal_700),
        actions = { actions() },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "arrow back",
                modifier = Modifier.clickable { goBackListener() }
            )
        },
    )
}

@Composable
fun FAB(onFABClicked: () -> Unit) {
    FloatingActionButton(
        onClick = { onFABClicked() }
    ) {
        Icon(Icons.Filled.Add, "")
    }
}

@Composable
fun Dialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(text = "Yes, leave")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(text = "No, stay")
            }
        },
        title = { Text(text = "Do you want to leave?") },
        text = { Text(text = "Your'e about to leave without saving the note") }
    )
}


@Composable
fun NoteDetailContent(
    title: String,
    body: String,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth(),
                value = title,
                textStyle = MaterialTheme.typography.h5,
                onValueChange = { onTitleChanged(it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                label = { Text(text = "Body") },
                modifier = Modifier.fillMaxSize(),
                value = body,
                textStyle = MaterialTheme.typography.body1,
                onValueChange = { onBodyChanged(it) }
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NoteDetailsScreen(navController = rememberNavController())
}