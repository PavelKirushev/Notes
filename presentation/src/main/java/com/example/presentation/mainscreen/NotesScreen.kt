package com.example.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.notes.domain.Note
import com.example.presentation.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable function for showing all notes on main screen
 *
 * @param controller NavHostController for navigation in app
 * @param mainViewModel MainViewModel to manage app state
 */
@Composable
fun NotesScreen(controller: NavHostController, mainViewModel: MainViewModel) {
    val listNote by mainViewModel.noteListFlow.collectAsState()
    val scope = CoroutineScope(Dispatchers.IO)
    LazyColumn {
        item {
            Row (
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(color = Color.Gray)
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = {
                    scope.launch {
                        mainViewModel.addNote(Note(mainViewModel.getNextNoteId(), "", ""))
                    }

                    controller.navigate("details/" + mainViewModel.getNextNoteId())
                }) {
                    Icon(painter = painterResource(R.drawable.edit_white),
                        contentDescription = "Edit",
                        modifier = Modifier
                            .size(30.dp)
                    )
                }

            }
        }

        items(
            items = listNote,
            key = { note -> note.id }
        ){note->
            CardNote(controller, note, mainViewModel)
        }
    }

}