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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.R
import com.alvaro.notes_compose.common.domain.Note
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NoteDetailsScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val note = viewModel.state.collectAsState().value.note

    var titleText: String by remember { mutableStateOf(note?.title ?: "") }
    var bodyText: String by remember { mutableStateOf(note?.content ?: "") }


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
                is UIComponent.Dialog -> {}
                is UIComponent.Toast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
                is UIComponent.Empty -> {}
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar {
                if (titleText.isNotEmpty() || bodyText.isNotEmpty()){
                    showAlertDialog = true
                }else{
                    navController.navigateUp()
                }
            }
        },
        content = {
            NoteDetailContent(
                title = titleText,
                body = bodyText,
                onTitleChanged = { titleText = it },
                onBodyChanged = { bodyText = it }
            )
        },
        floatingActionButton = {
            FAB {
                viewModel.triggerEvent(
                    NoteDetailsEvents.InsertNote(
                        Note(
                            title = titleText,
                            content = bodyText,
                            priority = 1,
                            timeStamp = ""
                        )
                    )
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    )
}

@Composable
fun Toolbar(goBackListener: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name), color = Color.White) },
        backgroundColor = colorResource(id = R.color.teal_700),
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
        onDismissRequest = {  onDismiss()  },
        confirmButton = {
            TextButton(
                onClick = {  onConfirm()  }
            ) {
                Text(text = "Yes, leave")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {  onDismiss()  }
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
                onValueChange = { onTitleChanged(it)  }
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