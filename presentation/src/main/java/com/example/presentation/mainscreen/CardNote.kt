package com.example.presentation.mainscreen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notes.domain.Note
import com.example.presentation.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun CardNote(controller: NavHostController, note: Note, mainViewModel: MainViewModel) {
    var checkDelete = remember { mutableStateOf(false) }
    Column {
        Card(
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.LightGray
            ),
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp
                )
                .fillMaxWidth()
                .height(70.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            controller.navigate("details/" + note.id)
                                },
                        onLongPress = {
                            checkDelete.value = true
                        }
                    )
                },
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 30.dp, top = 5.dp, end = 20.dp)
            ) {
                NoteTitleText(note.title)
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                NoteText(note.text)
            }
        }
        if (checkDelete.value) {
            DialogBeforeDelete(mainViewModel, note, checkDelete)
        }
    }
}