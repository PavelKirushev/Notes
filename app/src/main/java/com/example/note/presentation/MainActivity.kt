package com.example.note.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.note.data.Dependencies
import com.example.note.presentation.editnote.NoteWindow
import com.example.note.presentation.mainscreen.NotesScreen
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
            var note by remember { mutableStateOf(Note(-1, "...", "...")) }
            LaunchedEffect(Unit) {
                if(id != null) note = mainViewModel.getNote(id)
            }
            NoteWindow(controller, note, mainViewModel)
        }
    }
}


