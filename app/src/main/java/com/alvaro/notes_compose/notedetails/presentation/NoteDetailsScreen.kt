package com.alvaro.notes_compose.notedetails.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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
import com.alvaro.notes_compose.R
import com.alvaro.notes_compose.common.domain.NotePriority
import com.alvaro.notes_compose.common.domain.NoteType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val state by viewModel.screenState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { event ->
            when(event){
                is NoteDetailEffects.GoBack -> navController.navigateUp()
                is NoteDetailEffects.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is NoteDetailEffects.ShowAlert -> this@LaunchedEffect.launch {  }
            }
        }
    }

    UIContent(state = state, action = viewModel::setAction)
}

@Composable
private fun UIContent(state: NoteDetailState, action: (NoteDetailActions) -> Unit){
    when {
        state.isLoading -> LoadingContent()
        state.error != null -> ErrorContent(state)
        else -> ValidContent(state, action)
    }
}

@Composable
private fun ValidContent(state: NoteDetailState, action: (NoteDetailActions) -> Unit){

    Scaffold(
        topBar = {
            Toolbar(
                actions = {
                    SpinnerNoteActions(
                        notePrioritySelected = { action(NoteDetailActions.NotePrioritySelected(it) ) },
                        noteTypeSelected = { action(NoteDetailActions.NoteTypeSelected(it)) }
                    )
                },
                goBackListener = { action(NoteDetailActions.OnGoBackRequested) }
            )
        },
        content = {
            NoteDetailContent( state = state,   action = action )
        },
        floatingActionButton = {
            FAB {
                action(NoteDetailActions.OnAddNoteButtonClicked)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    )

    ShowAlertMessage(state = state, action = action)

}

@Composable
private fun ErrorContent(state: NoteDetailState){
    // implement logic
}

@Composable
private fun LoadingContent(){
    // implement logic
}

@Composable
private fun ShowAlertMessage(state: NoteDetailState, action: (NoteDetailActions) -> Unit){
    if (state.alertMessage != null) {
        Dialog(
            onDismiss = { action(NoteDetailActions.OnDismissAlert) },
            onConfirm = { action(NoteDetailActions.OnGoBackConfirmed) }
        )
    }
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
    state: NoteDetailState,
    action: (NoteDetailActions) -> Unit
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
                value = state.data.note.title,
                textStyle = MaterialTheme.typography.h5,
                onValueChange = { action(NoteDetailActions.OnUpdateTitle(it)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                label = { Text(text = "Body") },
                modifier = Modifier.fillMaxSize(),
                value = state.data.note.content,
                textStyle = MaterialTheme.typography.body1,
                onValueChange = { action(NoteDetailActions.OnUpdateBody(it)) }
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NoteDetailsScreen(navController = rememberNavController())
}