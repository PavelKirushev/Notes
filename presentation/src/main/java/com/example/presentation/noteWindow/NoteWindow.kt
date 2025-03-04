package com.example.presentation.noteWindow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.common.R
import com.example.notes.domain.Note
import com.example.presentation.MainViewModel
import kotlinx.coroutines.launch

//окно редактирования заметки
@Composable
fun NoteWindow(controller: NavHostController, note: Note, mainViewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf(note.title) }
    var text by remember { mutableStateOf(note.text) }
    var checkChange by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val noteCopy by remember { mutableStateOf(Note(note.id, note.title, note.text)) }

    // Обновляем заметку в MainViewModel при изменении title или text
    LaunchedEffect(title, text) {
        val updatedNote = note.copy(title = title, text = text)
        scope.launch {
            mainViewModel.editNote(updatedNote)
        }
    }

    Column(modifier = Modifier.padding(top = 20.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        if (checkChange) {
                            showDialog.value = true
                        } else {
                            if (title.isEmpty() && text.isEmpty()) {
                                scope.launch {
                                    mainViewModel.removeNote(note.id)
                                }
                            }
                            controller.navigateUp()
                        }

                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back_white),
                    contentDescription = "Назад",
                    Modifier.size(30.dp)
                )
                Text(text = "Назад", fontSize = 24.sp)
            }
            if (checkChange
                ) {
                Icon(
                    painter = painterResource(id = R.drawable.done),
                    contentDescription = "ОК",
                    Modifier
                        .size(45.dp)
                        .padding(top = 7.dp, end = 10.dp)
                        .clickable {
                            controller.navigateUp()
                        }
                )
            }
            if (showDialog.value) {
                DialogBeforeExit(controller, mainViewModel, noteCopy, showDialog,)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 30.dp, end = 20.dp, bottom = 30.dp)
        ) {
            SimpleTextField(
                text = title,
                fontSize = 40.sp,
                onValueChange = {
                    title = it
                    checkChange = true
                }
            )
            SimpleTextField(
                text = text,
                fontSize = 20.sp,
                onValueChange = {
                    text = it
                    checkChange = true
                }
            )
        }
    }
}