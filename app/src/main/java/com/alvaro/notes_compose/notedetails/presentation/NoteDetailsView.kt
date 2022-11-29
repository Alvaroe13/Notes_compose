package com.alvaro.notes_compose.notedetails.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alvaro.core.domain.UIComponent
import com.alvaro.notes_compose.common.domain.Note
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoteDetailsView(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    navController: NavController
) {

    val note = viewModel.state.collectAsState().value.note

    var titleText: String by remember { mutableStateOf(note?.title ?: "") }
    var bodyText: String by remember { mutableStateOf(note?.content ?: "") }
    val onAddNoteListener: () -> Unit = {
        viewModel.triggerEvent(
            NoteDetailsEvents.InsertNote(
                Note(title = titleText, content = bodyText, priority = 1, timeStamp = "")
            )
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.response.collectLatest { event ->
            when(event) {
                is UIComponent.Dialog -> {
                }
                is UIComponent.Toast -> {
                    navController.navigateUp()
                }
                else -> {}
            }
        }
    }

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
                value = titleText,
                textStyle = MaterialTheme.typography.h5,
                onValueChange = { titleText = it }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                label = { Text(text = "Body") },
                modifier = Modifier.fillMaxSize(),
                value = bodyText,
                textStyle = MaterialTheme.typography.body1,
                onValueChange = { bodyText = it }
            )

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
                    onClick = { onAddNoteListener() },
                    shape = RectangleShape,
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview(){
    NoteDetailsView(navController = rememberNavController())
}