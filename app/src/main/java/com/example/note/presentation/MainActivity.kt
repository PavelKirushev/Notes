package com.example.note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.data.Dependencies
import com.example.note.ui.theme.NoteTheme
import com.example.notes.data.NoteEntity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.init(this)
        setContent {
            NoteTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NotesScreen()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NotesScreen() {
    val notes = remember { mutableStateListOf<NoteEntity>() }
    val scope = rememberCoroutineScope()

    // Fetch notes from database
//    LaunchedEffect(Unit) {
//        scope.launch {
//            val myViewModel = MainViewModel()
//            val fetchedNotes = myViewModel.getNotesFromDatabase()
//            notes.clear()
//            notes.addAll(fetchedNotes)
//        }
//    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(text = "123",
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(30.dp)
                    .offset(x=15.dp, y=30.dp)
                    .border(border = BorderStroke(3.dp, Color.Red))
                    .background(Color.Green)
                    .padding(15.dp)
                    .shadow(1.dp, shape= CircleShape)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .padding(10.dp)

            )
        }
    }
}
