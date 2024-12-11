package com.example.note.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.note.data.Dependencies
import com.example.note.presentation.mainscreen.cardNote.CardNote
import com.example.note.ui.theme.NoteTheme
import com.example.notes.data.NoteListRepositoryImpl
import com.example.notes.domain.Note

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main(this)
        }
    }
}


@Composable
fun Main(context: Context){
    Dependencies.init(context)
    val mainViewModel = MainViewModel(NoteListRepositoryImpl(Dependencies.provideNoteDao()))
    val controller = rememberNavController()
    NoteTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//            NotesScreen(mainViewModel)
            Navigator(mainViewModel, controller)
        }
    }
}

@Composable
fun Navigator(mainViewModel: MainViewModel, controller: NavHostController, ){
    NavHost(navController = controller, startDestination = "home") {
        composable("home") { NotesScreen(controller, mainViewModel) }
        composable("details/{noteId}") {
            val id = it.arguments?.getString("noteId")?.toIntOrNull()
            var note by remember { mutableStateOf<Note>(Note(-1, "...", "...")) }
            LaunchedEffect(Unit) {
                if(id != null) note = mainViewModel.getNote(id)
            }
            NoteWindow(controller, note)
        }
    }
}

@Composable
fun NotesScreen(controller: NavHostController, mainViewModel: MainViewModel) {
    val listNote = remember { mutableStateListOf<Note>() }
    LaunchedEffect(Unit) {
        val notes = mainViewModel.getNoteList()
        listNote.clear()
        listNote.addAll(notes)
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(listNote.size){index->
            CardNote(controller, listNote.get(index),)
        }
    }
}


@Composable
fun NoteWindow(navHostController: NavHostController, note: Note){
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(top = 80.dp, start = 30.dp, end = 20.dp, bottom = 30.dp)
    ) {
        item {
            Text(text = note.title)
            Text(text = note.text)
        }
    }
}

