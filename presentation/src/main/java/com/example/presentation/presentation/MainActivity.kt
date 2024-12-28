package com.example.presentation.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.presentation.editnote.NoteWindow
import com.example.presentation.presentation.mainscreen.cardnote.NotesScreen
import com.example.presentation.presentation.theme.NoteTheme
import org.koin.androidx.compose.koinViewModel

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
    val mainViewModel: MainViewModel = koinViewModel()
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
            LaunchedEffect(Unit) {
                if(id != null) mainViewModel.getNote(id)
            }
            NoteWindow(controller, mainViewModel)
        }
    }
}


