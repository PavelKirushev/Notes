package com.example.note.presentation.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.note.R
import com.example.note.presentation.MainViewModel
import com.example.notes.domain.Note

//все заметки
@Composable
fun NotesScreen(controller: NavHostController, mainViewModel: MainViewModel) {
    val listNote = remember { mutableStateListOf<Note>() }
    LaunchedEffect(Unit) {
        val notes = mainViewModel.getNoteList()
        listNote.clear()
        listNote.addAll(notes)
    }
    Column {
        Row (
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = Color.Gray)
                .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = {TODO("ALERT DIALOG")}) {
                Icon(painter = painterResource(R.drawable.edit_white),
                    contentDescription = "Edit",
                    modifier = Modifier
                        .size(30.dp)
                )
            }

        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, bottom = 20.dp)
        ) {
            items(listNote.size){index->
                CardNote(controller, listNote.get(index),)
            }
        }
    }

}