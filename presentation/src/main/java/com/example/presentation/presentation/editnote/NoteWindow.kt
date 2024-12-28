package com.example.presentation.presentation.editnote

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.common.R
import com.example.presentation.presentation.MainViewModel
import kotlinx.coroutines.launch

//окно редактирования заметки
@Composable
fun NoteWindow(controller: NavHostController, mainViewModel: MainViewModel){
    val scope = rememberCoroutineScope()
    val note by mainViewModel.noteFlow.collectAsState()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 30.dp, end = 20.dp, bottom = 30.dp)
                .clickable { TODO() }

        ) {
            Column {
                SimpleTextField(text = note.title, fontSize = 40.sp, onValueChange = {
                    scope.launch {
                        mainViewModel.editNote(note.copy(title = it))
                    }
                }, )

                SimpleTextField(text = note.text, fontSize = 20.sp, onValueChange = {
                    scope.launch {
                        mainViewModel.editNote(note.copy(text = it))
                    }
                }, )
            }


        }
    }
}


@Composable
fun SimpleTextField(
    text: String,
    onValueChange: (String) -> Unit,
    fontSize: TextUnit
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.Black, fontSize = fontSize),
        singleLine = false,
        modifier = Modifier.fillMaxWidth(),
    )
}