package com.example.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.presentation.noteWindow.NoteWindow
import com.example.presentation.mainscreen.NotesScreen


/**
 * Composable function for navigation in app
 *
 *  home - main screen (NotesScreen.kt)
 *
 *  details - window for editing note (NoteWindow.kt)
 */
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Navigator(mainViewModel: MainViewModel, controller: NavHostController, ){
    NavHost(navController = controller, startDestination = "home") {
        composable("home") { NotesScreen(controller, mainViewModel) }
        composable("details/{noteId}") {
            val noteId = it.arguments?.getString("noteId")?.toIntOrNull()
            if (noteId != null) {
                val note = mainViewModel.noteListFlow.value.find { it.id == noteId }
                if (note != null) {
                    NoteWindow(controller, note, mainViewModel)
                }
            }

        }
    }
}