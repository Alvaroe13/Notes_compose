package com.alvaro.notes_compose.notedetails.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Preview(showBackground = true)
@Composable
fun NoteDetailsView(viewModel: NoteDetailViewModel = hiltViewModel()) {

    val note = viewModel.state.collectAsState().value.note

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red)
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = note?.title ?: "Title",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text =  note?.content ?: "Body",
                style = MaterialTheme.typography.subtitle1
            )
        }

    }
}
