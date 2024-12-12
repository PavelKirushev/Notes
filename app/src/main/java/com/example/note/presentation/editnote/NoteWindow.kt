package com.example.note.presentation.editnote

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.presentation.MainViewModel
import com.example.note.presentation.common.NoteText
import com.example.note.presentation.common.NoteTitleText
import com.example.notes.domain.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//окно редактирования заметки
@Composable
fun NoteWindow(controller: NavHostController, note: Note, mainViewModel: MainViewModel){
    val scope = rememberCoroutineScope()
    Column (modifier = Modifier
        .padding(top = 20.dp)){
        Row(
            Modifier
                .padding(10.dp)
                .clickable {
                    controller.navigate("home")
                },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_white),
                contentDescription = "Назад",
                Modifier.size(30.dp)
            )
            Text(text = "Назад", fontSize = 24.sp)

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 30.dp, end = 20.dp, bottom = 30.dp)
                .clickable { TODO() }

        ) {
            item {
                Column {
                    SimpleTextField(text = note.title, onValueChange = {
                        scope.launch {
                            mainViewModel.editNote(Note(note.id, it, note.text))
                        }

                    })
                    NoteTitleText(note.title, fontSize = 50.sp, color = Color.Black, overflow = TextOverflow.Clip)
                    NoteText(note.text, fontSize = 20.sp, color = Color.Black, overflow = TextOverflow.Clip)
                }

            }
        }
    }
}


@Composable
fun SimpleTextField(
    text: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        singleLine = false,
        modifier = Modifier
    ) { innerTextField ->
        // Просто отображаем текст без дополнительных элементов
        innerTextField()
    }
}